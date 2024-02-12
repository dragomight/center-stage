package org.firstinspires.ftc.teamcode.BillsAmazingArm;

import android.util.Log;

import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.MotorPool;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UtilityKit;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;

/**
 * The arm tracker, records and updates the target position and velocity of the arm.
 * It verifies that arm angle limits are not exceeded by a target.
 * It relays information to the MotorPool (motor controller) when update is called
 */
public class ArmController {

    // Independent variables
    private ArmPose targetPose = new ArmPose(); // the current target position of the arm, where it is trying to move to
    private ArmPose targetVelocity = new ArmPose(); // the current target velocity (if applicable, i.e. in run to velocity mode)

    private ArmControlMode controlMode = ArmControlMode.RUN_TO_POSITION;

    private Cadbot cadbot;
    private MotorPool motorPool;

    private Vector2D correction = new Vector2D(); // the backlash correction in radians for th1 and th2

    public final static double SERVO_CONV = Math.PI*300/180;

    public ArmController(Cadbot cadbot){
        this.cadbot = cadbot;
        this.motorPool = cadbot.motorPool;
        resetHome();

        Log.e("ArmController", ArmPoseXZ.report());
    }

    // reset home specifies that the current motor position, which is the target, is the home position
    // that way it is not trying to run to some other position yet.
    public void resetHome(){
        targetPose = new ArmPose(ArmConstants.TH1HOME,
                                ArmConstants.TH2HOME,
                                ArmConstants.TH3HOME,
                                ArmConstants.TH4HOME);
    }

    public ArmControlMode getControlMode(){
        return controlMode;
    }

    public ArmPose getTargetPose(){
        return targetPose.copy();
    }

    public ArmPose getTargetVelocity(){
        return targetVelocity.copy();
    }

    // Gets the current arm pose from the MotorPool in radians
    public ArmPose getPose(){
        // get the motor position data
        ArmPose ap = new ArmPose(
                ticksToRadians(motorPool.getJoint1Ticks()) + ArmConstants.TH1HOME,
                ticksToRadians(motorPool.getJoint2Ticks()) + ArmConstants.TH2HOME,
                targetPose.th3,
                targetPose.th4);

        // apply the previous backlash correction to a copy of the motor read
        ArmPose ap2 = ap.copy();
        ap2.th1 += correction.getX();
        ap2.th2 += correction.getY();

        // calculate the new backlash correction using the previous backlash correction
        correction = BacklashCorrection.getCorrection(ap2);

        // apply the new backlash correction to the current motor read
        ap.th1 += correction.getX();
        ap.th2 += correction.getY();
        return ap;
    }

    // Gets the current arm velocity from the MotorPool in radians per second
    public ArmPose getVelocity(){
        return new ArmPose(ticksToRadians(motorPool.getJoint1TicksPerSecond()),
                            ticksToRadians(motorPool.getJoint2TicksPerSecond()),
                            0, 0); // todo: track servo speed
    }

    // Converts the input number from the range [-PI/2, PI/2] to the range [0.0, 1.0] should be 300deg not 180
    public double convertRangeToServo(double radians){
        return radians/SERVO_CONV + 0.5;
    }

    public double convertRangeFromServo(double input){
        return (input - 0.5) * SERVO_CONV;
    }

    public static double ticksToRadians(double ticks){
        return ticks * ArmConstants.RADIANS_PER_TICK;
    }

    public static int radiansToTicks(double radians){
        return (int) (radians / ArmConstants.RADIANS_PER_TICK);
    }

//    public boolean doesSpeedExceedLimits(double omega1, double omega2, double avgDt){
//        double th1Next = pose.th1 + omega1 * avgDt;
//        double th2Next = pose.th2 + omega2 * avgDt;
//        return th1Next > ArmConstants.TH1MAX || th1Next < ArmConstants.TH1MIN ||
//                th2Next > ArmConstants.TH2MAX || th2Next < ArmConstants.TH2MIN;
//    }

    // stores the arms target position in radians, which is limited to a valid range, assuming home is set correctly
    // it then relays the position to the MotorPool
    public void setTargetPosition(double radians1, double radians2, double radians3, double radians4){

        // limit the target to be within the valid range of motion
        targetPose.th1 = UtilityKit.limitToRange(radians1, ArmConstants.TH1MIN, ArmConstants.TH1MAX);
        targetPose.th2 = UtilityKit.limitToRange(radians2, ArmConstants.TH2MIN, ArmConstants.TH2MAX);
        targetPose.th3 = UtilityKit.limitToRange(radians3, ArmConstants.TH3MIN, ArmConstants.TH3MAX);
        targetPose.th4 = UtilityKit.limitToRange(radians4, ArmConstants.TH4MIN, ArmConstants.TH4MAX);

        // send the results to the motor pool after removing the backlash correction and home position
        motorPool.setJointPositions(ArmController.radiansToTicks(targetPose.th1 - ArmConstants.TH1HOME - correction.getX()),
                                    ArmController.radiansToTicks(targetPose.th2 - ArmConstants.TH2HOME - correction.getY()));
        motorPool.setRockJointPosition(convertRangeToServo(targetPose.th3));
        motorPool.setRollJointPosition(convertRangeToServo(targetPose.th4));
    }

//    // will set the motor speeds... this has problems for servo control to be worked out later, so don't use now
//    public void setTargetVelocity(ArmPose targetVelocity, double avgDt){
//        motorPool.setJointsTicksPerSecond(targetVelocity.th1, targetVelocity.th2);
////        motorPool.setRockJointPosition(convertRangeToServo(pose.th3 + targetVelocity.th3 * avgDt)); // move the servos at a set speed
////        motorPool.setRollJointPosition(convertRangeToServo(pose.th4 + targetVelocity.th4 * avgDt));
//    }
}
