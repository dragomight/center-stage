package org.firstinspires.ftc.teamcode.BillsAmazingArm;

public class ArmPoseXZ {
    public double x; // the position of the finger tip
    public double z; // the position of the finger tip
    public double th3; // servo rock or pitch joint
    public double th4; // servo roll joint

    public ArmPoseXZ(){

    }

    public ArmPoseXZ(double x, double z, double th3, double th4){
        this.x = x;
        this.z = z;
        this.th3 = th3;
        this.th4 = th4;
    }

    public static ArmPoseXZ home(){
        return new ArmPoseXZ(5.0, 0.0, Math.toRadians(90.0), 0);
    }

    public static ArmPoseXZ ready(){
        return new ArmPoseXZ(5.0, 7.0, Math.toRadians(90.0), 0); // pitch should be calculated from arm angles
    }

    public static ArmPoseXZ forward(){
        return new ArmPoseXZ(11.0, 7.0, Math.toRadians(90.0), 0);
    }

    public static ArmPoseXZ pickUpAt(double x, double z){
        return new ArmPoseXZ(x, z, Math.toRadians(90.0), 0);
    }
}
