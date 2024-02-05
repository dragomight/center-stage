package org.firstinspires.ftc.teamcode.BillsAmazingArm;

public class ArmPose {
    // Independent variables
    public double th1; // the base joint
    public double th2; // the elbow joint
    public double th3; // the servo or wrist joint
    public double th4; // the servo roll joint

    public ArmPose(){
    }

    public ArmPose(double th1, double th2, double th3, double th4){
        this.th1 = th1;
        this.th2 = th2;
        this.th3 = th3;
        this.th4 = th4;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(Math.toDegrees(th1) + ", ");
        sb.append(Math.toDegrees(th2) + ", ");
        sb.append(Math.toDegrees(th3) + ", ");
        sb.append(Math.toDegrees(th4) + "]");
        return sb.toString();
    }

    public ArmPose copy(){
        return new ArmPose(th1, th2, th3, th4);
    }
}
