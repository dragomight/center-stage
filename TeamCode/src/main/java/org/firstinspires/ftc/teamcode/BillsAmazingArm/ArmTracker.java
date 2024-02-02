package org.firstinspires.ftc.teamcode.BillsAmazingArm;

import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.MotorPool;

/**
 * The arm tracker, records and updates the position and velocity of the arm
 */
public class ArmTracker {

    // Independent variables
    private ArmPose pose = new ArmPose();
    private ArmPose velocity = new ArmPose();

    private Cadbot cadbot;
    private MotorPool motorPool;

    public final static double SERVO_CONV = Math.PI*300/180;

    public ArmTracker(Cadbot cadbot){
        this.cadbot = cadbot;
        this.motorPool = cadbot.motorPool;
    }

    public void update(){
        // get the motor position data
        pose.th1 = ticksToRadians(motorPool.getJoint1Ticks()) + ArmConstants.TH1HOME;
        pose.th2 = ticksToRadians(motorPool.getJoint2Ticks()) + ArmConstants.TH2HOME;

        // get the motor velocity data
        velocity.th1 = ticksToRadians(motorPool.getJoint1TicksPerSecond());
        velocity.th2 = ticksToRadians(motorPool.getJoint2TicksPerSecond());

        // get the servo motor data
        pose.th3 = convertRangeFromServo(motorPool.getRockJointPosition());
        pose.th4 = convertRangeFromServo(motorPool.getRollJointPosition());
    }

    public ArmPose getPose(){
        return pose;
    }

    public ArmPose getVelocity(){
        return velocity;
    }

    // will set the motor speeds... this has problems for servo control to be worked out later, so don't use now
    public void setVelocity(ArmPose targetVelocity, double avgDt){
        motorPool.setJoint1TicksPerSecond(targetVelocity.th1);
        motorPool.setJoint2TicksPerSecond(targetVelocity.th2);
        motorPool.setRockJointPosition(convertRangeToServo(pose.th3 + targetVelocity.th3 * avgDt)); // move the servos at a set speed
        motorPool.setRollJointPosition(convertRangeToServo(pose.th4 + targetVelocity.th4 * avgDt));
    }

    public void setVelocity2(ArmPose targetVelocity, double avgDt){
        motorPool.setJoint1TicksPerSecond(targetVelocity.th1);
        motorPool.setJoint2TicksPerSecond(targetVelocity.th2);
        motorPool.setRockJointPosition(convertRangeToServo(targetVelocity.th3)); // move the servos to position like normally
        motorPool.setRollJointPosition(convertRangeToServo(targetVelocity.th4)); // these are not actually velocities
    }

    // Converts the input number from the range [-PI/2, PI/2] to the range [0.0, 1.0] should be 300deg not 180
    public double convertRangeToServo(double input){
        return input/SERVO_CONV + 0.5;
    }

    public double convertRangeFromServo(double input){
        return (input - 0.5) * SERVO_CONV;
    }

    public static double ticksToRadians(double ticks){
        return ticks * ArmConstants.RADIANS_PER_TICK;
    }

    public boolean exceedsLimits(double omega1, double omega2, double avgDt){
        double th1Next = pose.th1 + omega1 * avgDt;
        double th2Next = pose.th2 + omega2 * avgDt;
        return th1Next > ArmConstants.TH1MAX || th1Next < ArmConstants.TH1MIN ||
                th2Next > ArmConstants.TH2MAX || th2Next < ArmConstants.TH2MIN;
    }
}
