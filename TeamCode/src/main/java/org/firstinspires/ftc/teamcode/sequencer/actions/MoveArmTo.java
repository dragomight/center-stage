package org.firstinspires.ftc.teamcode.sequencer.actions;

import android.util.Log;

import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmConstants;
import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmPose;
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
    private boolean done = false;
    private double rock, roll; // the target pitch and roll of the gripper
    private ArmPose pose; // the current pose

    private Vector2D tipTarget; // where the tip of the gripper will end up
    private Vector2D tipPosition = new Vector2D(); // where the tip is now if it were at its target pitch (rock)
    private Vector2D j3LastPose = new Vector2D();
    private Vector2D targetSpeed = new Vector2D();

    public MoveArmTo(Cadbot cadbot, double x, double z, double rock, double roll){
        this.cadbot = cadbot;
        this.armTracker = cadbot.armTracker;
        this.rock = rock;
        this.roll = roll;
        tipTarget = new Vector2D(x, z);
    }

    @Override
    public void update() {
        // get the current arm pose
        pose = armTracker.getPose();
        double dt = cadbot.deadWheelTracker.getTime();

        // kinematic update (tells us the end point position of the tip relative to robot center):
        // Position of the third joint calculated from angles by kinematics
        Vector2D j3Pose = Kinematics.j3(pose);
        // Velocity is updated
        Vector2D j3Vel = j3Pose.subtract(j3LastPose);
        j3Vel.divideBy(dt);
        double speed = j3Vel.magnitude();

        // Calculating the tip position from joint angles using kinematics (maybe here we should use the eventual position of the arm's j3, since that one is moving)
        ArmPose poseMod = new ArmPose(pose.th1, pose.th2, rock, roll); // we use the mod for where the tip will be
        tipPosition = Kinematics.tip(poseMod); // so this is not the tip pose at the present time

        // vector to the target from the tip
        Vector2D toTarget = tipTarget.subtract(tipPosition);

        double distance = toTarget.magnitude();

        double avgDt = cadbot.deadWheelTracker.getAvgDt();

        // if close enough to the target arm pose, stop the motion and hold the pose but the task is done
        if(distance < ArmConstants.DISTANCE_THRESHOLD){ // todo: this assumes the pose is reachable
            armTracker.setVelocity2(new ArmPose(0.0, 0.0, rock, roll), avgDt);
            done = true;
            Log.e("MoveArmTo", "Action complete at d=" + distance + "   tipTarget=" + tipTarget + "   tipPose=" + tipPosition);
            return;
        }

        // UPDATE THE TARGET ARM SPEEDS
        // calculate the stopping distance for the current speed
        double dStopX = -.5 * ArmConstants.AMAX * avgDt * avgDt * Math.signum(j3Vel.getX()) + j3Vel.getX() * avgDt;
        double dStopY = -.5 * ArmConstants.AMAX * avgDt * avgDt * Math.signum(j3Vel.getY()) + j3Vel.getY() * avgDt;
        Vector2D dStop = new Vector2D(dStopX, dStopY);
//        Vector2D unit = toTarget.unitVector();
//        unit.multiplyBy(ArmConstants.AMAX);

        if(distance < dStop.magnitude()){
            // time to slow down
            targetSpeed.set(j3Vel.getX() - ArmConstants.AMAX * avgDt * Math.signum(j3Vel.getX()),
                            j3Vel.getY() - ArmConstants.AMAX * avgDt * Math.signum(j3Vel.getY()));
        }
        else {
            // time to speed up
            targetSpeed.set(j3Vel.getX() + ArmConstants.AMAX * avgDt * Math.signum(j3Vel.getX()),
                            j3Vel.getY() + ArmConstants.AMAX * avgDt * Math.signum(j3Vel.getY()));
        }

        // calculate the target angle velocities
        Vector2D thDot = InverseKinematics.thDot(
                ArmConstants.L1, ArmConstants.L2, j3Pose.getX(), j3Pose.getY(),
                targetSpeed.getX(), targetSpeed.getY(), pose.th1, pose.th2, UnitOfAngle.RADIANS);

        // check that these speeds will not cause the joints to exceed their limits
        if(armTracker.exceedsLimits(thDot.getX(), thDot.getY(), avgDt)){
            // emergency stop
            armTracker.setVelocity2(new ArmPose(0.0, 0.0, rock, roll), avgDt);
            Log.e("MoveArmTo", "Emergency Stop at d=" + distance + "   tipTarget=" + tipTarget + "   tipPose=" + tipPosition + "   pose=" + pose);

            done = true;
        }
        else {
            // check that the anticipated positions are not outside the allowed ranges
            armTracker.setVelocity2(new ArmPose(thDot.getX(), thDot.getY(), rock, roll), avgDt);
        }
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
