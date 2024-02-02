package org.firstinspires.ftc.teamcode.sequencer.actions;

import android.util.Log;

import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmConstants;
import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmPose;
import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmPoseXZ;
import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmTracker;
import org.firstinspires.ftc.teamcode.BillsAmazingArm.InverseKinematics;
import org.firstinspires.ftc.teamcode.BillsAmazingArm.Kinematics;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UnitOfAngle;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;

public class MoveArmTo implements RobotAction{

    private Cadbot cadbot;
    private ArmTracker armTracker;
    private ArmPose targetPose;
    private ArmPoseXZ targetArmPoseXZ;
    private boolean done = false;
    private ArmPose pose; // the current pose
    private boolean firstTime = true;

    private Vector2D tipTarget = new Vector2D(); // where the tip of the gripper will end up
    private Vector2D tipPosition = new Vector2D(); // where the tip is now if it were at its target pitch (rock)
    private Vector2D tipVel = new Vector2D();
    private Vector2D lastTipPose = new Vector2D();
    private Vector2D j3LastPose = new Vector2D();
    private Vector2D targetSpeed = new Vector2D();
    Vector2D j3Vel;

    public MoveArmTo(Cadbot cadbot, ArmPoseXZ targetArmPoseXZ){
        this.cadbot = cadbot;
        this.armTracker = cadbot.armTracker;
        this.targetArmPoseXZ = targetArmPoseXZ;

        tipTarget = new Vector2D(targetArmPoseXZ.x, targetArmPoseXZ.z);

        //InverseKinematics.test();
    }

    @Override
    public void update() {
        // PHASE I: GET THE BASIC DATA
        // get the time interval
        double dt = cadbot.deadWheelTracker.getTime();
        double avgDt = cadbot.deadWheelTracker.getAvgDt();

        // get the current arm pose (in angles)
        pose = armTracker.getPose();

        // Position of the third joint calculated from angles by kinematics (robot coordinates)
        Vector2D j3Pose = Kinematics.j3(pose); // home is 4.68, 7.75

        // Create a modified pose that has the current joint angles plus anticipated wrist angle
        ArmPose poseMod = new ArmPose(pose.th1, pose.th2, targetArmPoseXZ.getWristAngle(pose), targetArmPoseXZ.th4);

        // Now we get the current tip position
        tipPosition = Kinematics.tip(poseMod);

        // vector to the target from the tip
        Vector2D toTipTarget = tipTarget.copy().subtract(tipPosition);

        double distance = toTipTarget.magnitude();

        // Velocity in (x,z) is updated for joint 3 and the tip
        if(firstTime){
            j3LastPose = j3Pose.copy();
            firstTime = false;
            j3Vel = new Vector2D(); // assumes we are starting at a zero velocity
            lastTipPose = tipPosition.copy();
            tipVel = new Vector2D();
        }
        else {
            j3Vel = j3Pose.copy().subtract(j3LastPose).divideBy(dt);
            j3LastPose = j3Pose.copy();
            tipVel = tipPosition.copy().subtract(lastTipPose).divideBy(dt);
            lastTipPose = tipPosition.copy();
        }

        double speed = tipVel.magnitude();

        // PHASE II: TEST IF DONE
        // if close enough to the target arm pose, stop the motion and hold the pose but the task is done
        if(distance < ArmConstants.DISTANCE_THRESHOLD){ // todo: this assumes the pose is reachable, we may rather do angle thresholds
            armTracker.setVelocity2(new ArmPose(0.0, 0.0, poseMod.th3, poseMod.th4), avgDt);
            done = true;
            Log.e("MoveArmTo", "Action complete at d=" + distance + "   tipTarget=" + tipTarget + "   tipPose=" + tipPosition + "  tipVel=" + tipVel);
            return;
        }
        Log.e("MoveArmTo", "   tipTarget=" + tipTarget + "   tipPose=" + tipPosition + "  toTipTarget=" + toTipTarget + "  d=" + distance + "  tipVel=" + tipVel + "  speed=" + speed);

        Log.e("MoveArmTo", "  pose=" + pose + "  j3Pose=" + j3Pose + "  d=" + distance);

        // PHASE III: CALCULATE THE TARGET SPEEDS
        // UPDATE THE TARGET ARM SPEEDS
        Vector2D unitToTarget = toTipTarget.unitVector();
        Vector2D acc;
        // stopping distance
        double dStop = speed * speed / (2.0 * ArmConstants.AMAX);
        if(distance < dStop){
            // slow down
            acc = unitToTarget.copy().multiplyBy(-ArmConstants.AMAX);
        }
        else{
            if(speed > ArmConstants.VMAX){
                acc = new Vector2D(); // no accel
            }
            else {
                acc = unitToTarget.copy().multiplyBy(ArmConstants.AMAX);
            }
        }
        acc.multiplyBy(avgDt);
        targetSpeed.add(acc);
        targetSpeed.limitMagnitude(ArmConstants.VMAX);

//        // calculate the stopping distance for the current speed
//        double dStopX = -.5 * ArmConstants.AMAX * avgDt * avgDt * Math.signum(j3Vel.getX()) + j3Vel.getX() * avgDt;
//        double dStopY = -.5 * ArmConstants.AMAX * avgDt * avgDt * Math.signum(j3Vel.getY()) + j3Vel.getY() * avgDt;
//        Vector2D dStop = new Vector2D(dStopX, dStopY);
////        Vector2D unit = toTarget.unitVector();
////        unit.multiplyBy(ArmConstants.AMAX);
//        Log.e("MoveArmTo", "dStop=" + dStop);
//        if(distance < dStop.magnitude()){
//            Log.e("MoveArmTo", "Slowing Down: ");
//            // time to slow down
//            targetSpeed.set(j3Vel.getX() - ArmConstants.AMAX * avgDt * Math.signum(j3Vel.getX()),
//                            j3Vel.getY() - ArmConstants.AMAX * avgDt * Math.signum(j3Vel.getY()));
//        }
//        else {
//            // time to speed up
//            Log.e("MoveArmTo", "Speeding Up: ");
//            targetSpeed.set(j3Vel.getX() + ArmConstants.AMAX * avgDt * Math.signum(j3Vel.getX()),
//                            j3Vel.getY() + ArmConstants.AMAX * avgDt * Math.signum(j3Vel.getY()));
//        }

        // PHASE IV: CALCULATE THE TARGET ANGULAR VELOCITIES
        // get the anticipated new position
        Vector2D nextTipPose = tipPosition.copy().add(targetSpeed.copy().multiplyBy(avgDt));
        // don't forget to remove the offset created by the location of the first joint
        nextTipPose.subtract(ArmConstants.L0x, ArmConstants.L0z);
        // use inverse kinematics to calculate the joint angles for that new position (and hope for the best)
        Vector2D theta = InverseKinematics.theta(ArmConstants.L1, ArmConstants.L2, nextTipPose.getX(), nextTipPose.getY());
        // create a velocity that will get us there (maybe)
        Vector2D thetaDot = new Vector2D(theta.getX() - pose.th1, theta.getY() - pose.th2); // that's confusing, next angle - last angle
        thetaDot.divideBy(avgDt);
        Log.e("MoveArmTo", "nextTipPose=" + nextTipPose + "  theta=" + theta + "  thetaDot=" + thetaDot);
//        Vector2D thDot = InverseKinematics.thDot(
//                ArmConstants.L1, ArmConstants.L2, j3Pose.getX(), j3Pose.getY(),
//                targetSpeed.getX(), targetSpeed.getY(), pose.th1, pose.th2, UnitOfAngle.RADIANS);

        Log.e("MoveArmTo", "thDot=" + thetaDot + "  from targetSpeed=" + targetSpeed);

        // PHASE V: CHECK THAT ANGULAR SPEEDS WILL NOT CAUSE JOINTS TO EXCEED THEIR LIMITS
        if(armTracker.exceedsLimits(thetaDot.getX(), thetaDot.getY(), avgDt)){
            // emergency stop
            armTracker.setVelocity2(new ArmPose(0.0, 0.0, poseMod.th3, poseMod.th4), avgDt);
            Log.e("MoveArmTo", "Emergency Stop at d=" + distance + "   tipTarget=" + tipTarget + "   tipPose=" + tipPosition + "   pose=" + pose + "  thDot=" + thetaDot + "  avgDt=" + avgDt);
            done = true;
            return;
        }

        // PHASE VI: SET ANGULAR VELOCITIES
        armTracker.setVelocity2(new ArmPose(thetaDot.getX(), thetaDot.getY(), poseMod.th3, poseMod.th4), avgDt);
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
