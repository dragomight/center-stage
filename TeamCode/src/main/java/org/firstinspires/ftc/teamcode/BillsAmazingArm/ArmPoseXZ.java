package org.firstinspires.ftc.teamcode.BillsAmazingArm;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;

public class ArmPoseXZ {
    public double x; // the position of the finger tip
    public double z; // the position of the finger tip
    public double th3; // servo rock or pitch joint
    public double th4; // servo roll joint
    public double tilt; // a tilt angle relative to the robot, used to set th3
    private boolean useTilt = false; // th3 is absolute when not using the tilt, otherwise relative to the robot
    // 0 is up, 180 is forward down, -180 is backward down, 120 is perpendicular to the backdrop

    public ArmPoseXZ(){

    }

    // use this to construct an absolute target
    public ArmPoseXZ(double x, double z, double th3, double th4){
        this.x = x;
        this.z = z;
        this.th3 = th3;
        this.th4 = th4;
    }

    // use this to construct a tilt relative target
    public ArmPoseXZ(double x, double z, double th3, double th4, double tilt){
        this.x = x;
        this.z = z;
        this.th3 = th3;
        this.th4 = th4;
        this.tilt = tilt;
        useTilt = true;
    }

    // gets the home pose
    public static ArmPoseXZ home(){
        return new ArmPoseXZ(5.0, 0.0, Math.toRadians(0.0), 0, Math.toRadians(0));
    }

    // gets the ready pose (note it must be forward slightly of home or it is not physically realizable)
    public static ArmPoseXZ ready(){
        return new ArmPoseXZ(5.5, 7.0, Math.toRadians(0.0), 0, Math.toRadians(0));
    }

    public static ArmPoseXZ forward(){
        return new ArmPoseXZ(11.0, 7.0, Math.toRadians(90.0), 0, Math.toRadians(180));
    }

    public static ArmPoseXZ pickUpAtFront(double x, double z){
        return new ArmPoseXZ(x, z, Math.toRadians(90.0), 0, Math.toRadians(180));
    }

    public static ArmPoseXZ pickUpAtBack(double x, double z){
        return new ArmPoseXZ(x, z, 0.0, 0.0, Math.toRadians(-180));
    }

    public static ArmPoseXZ placeOnBackdrop(double x, double z){
        return new ArmPoseXZ(x, z, Math.toRadians(90.0), 0, Math.toRadians(120));
    }

    public static ArmPoseXZ from(ArmPose armPose){
        Vector2D v = Kinematics.tip(armPose);
        return new ArmPoseXZ(v.getX(), v.getY(), armPose.th3, armPose.th4);
    }

    public static ArmPoseXZ from(ArmPose armPose, double tilt){
        Vector2D v = Kinematics.tip(armPose);
        return new ArmPoseXZ(v.getX(), v.getY(), armPose.th3, armPose.th4, tilt);
    }

    // to point down in front is tilt=180, so th1=-90, th2=180, yields th3=90
    // to point down in back is tilt=-180, so th1=-90, th2=0, yields th3=-90
    public double getWristAngle(ArmPose armPose){
        if(useTilt){
            th3 = tilt - armPose.th1 - armPose.th2;
        }
        return th3;
    }
}
