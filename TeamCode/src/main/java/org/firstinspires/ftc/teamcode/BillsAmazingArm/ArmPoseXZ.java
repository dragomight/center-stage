package org.firstinspires.ftc.teamcode.BillsAmazingArm;

import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;

public class ArmPoseXZ {
    public double x; // the position of the finger tip
    public double z; // the position of the finger tip
    public double th3; // servo rock or pitch joint
    public double th4; // servo roll joint
    public double tilt; // a tilt angle relative to the robot, used to set th3
    private boolean useTilt = false; // th3 is absolute when not using the tilt, otherwise relative to the robot
    // 0 is up, 180 is forward down, -180 is backward down, 120 is perpendicular to the backdrop

    // to point down in front is tilt=180, so th1=-90, th2=180, yields th3=90
    // to point down in back is tilt=-180, so th1=-90, th2=0, yields th3=-90
    public final static double TILT_DOWN_FRONT = Math.toRadians(180);
    public final static double TILT_DOWN_BACK = Math.toRadians(-180);
    public final static double TILT_BACKDROP_FRONT = Math.toRadians(120);
    public final static double UP = 0;

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
        return new ArmPoseXZ(5.0, 0.0, Math.toRadians(0.0), 0, TILT_DOWN_FRONT);
    }

    public static ArmPoseXZ ready2(){
        return new ArmPoseXZ(5.0, 8.0, Math.toRadians(0.0), 0, TILT_DOWN_FRONT);
    }

    // gets the ready pose (note it must be forward slightly of home or it is not physically realizable) // 16 and 25 deg up
    public static ArmPoseXZ ready(){
        double th1 = Math.toRadians(-90+20);
        double th2 = Math.toRadians(180-50);
        return from(new ArmPose(th1, th2, TILT_DOWN_FRONT - th1 - th2, 0), TILT_DOWN_FRONT);
        //return new ArmPoseXZ(5.5, 7.0, Math.toRadians(0.0), 0,TILT_DOWN_FRONT);
    }

    public static ArmPoseXZ carry(){
        double th1 = Math.toRadians(-90);
        double th2 = Math.toRadians(180-5);
        return from(new ArmPose(th1, th2, TILT_DOWN_FRONT - th1 - th2, 0), TILT_DOWN_FRONT);
        //return new ArmPoseXZ(5.5, 7.0, Math.toRadians(0.0), 0,TILT_DOWN_FRONT);
    }

    public static ArmPoseXZ straightUp(){
        double th1 = Math.toRadians(0);
        double th2 = Math.toRadians(0);
        return from(new ArmPose(th1, th2, 0, 0));
    }

    public static ArmPoseXZ hanging(){
        double th1 = Math.toRadians(-90);
        double th2 = Math.toRadians(120);
        return from(new ArmPose(th1, th2, 0, 0));
    }

    public static ArmPoseXZ passingOverForward(){
        double th1 = Math.toRadians(-90);
        double th2 = Math.toRadians(90);
        return from(new ArmPose(th1, th2, 0, 0));
    }

    public static ArmPoseXZ passingOverBackward(){
        double th1 = Math.toRadians(0);
        double th2 = Math.toRadians(-90);
        return from(new ArmPose(th1, th2, 0, 0));
    }

    public static ArmPoseXZ forward(){
        return new ArmPoseXZ(14.0, 8.0, Math.toRadians(90.0), 0, TILT_DOWN_FRONT);
    }

    public static ArmPoseXZ backward(){
        return new ArmPoseXZ(-14.0, 8.0, Math.toRadians(-90.0), 0, TILT_DOWN_BACK);
    }

    public static ArmPoseXZ reachingBackward(double x, double z){
        return new ArmPoseXZ(x, z, Math.toRadians(-90.0), 0, TILT_DOWN_BACK);
    }

    public static ArmPoseXZ reachingForward(double x, double z){
        return new ArmPoseXZ(x, z, Math.toRadians(90.0), 0, TILT_DOWN_FRONT);
    }

    public static ArmPoseXZ placeOnBackdrop(double x, double z){
        return new ArmPoseXZ(x, z, Math.toRadians(90.0), 0, TILT_BACKDROP_FRONT);
    }

    public static ArmPoseXZ from(ArmPose armPose){
        Vector2D v = Kinematics.tip(armPose);
        return new ArmPoseXZ(v.getX(), v.getY(), armPose.th3, armPose.th4);
    }

    public static ArmPoseXZ from(ArmPose armPose, double tilt){
        Vector2D v = Kinematics.tip(armPose);
        ArmPoseXZ armPoseXZ = new ArmPoseXZ(v.getX(), v.getY(), armPose.th3, armPose.th4, tilt);
        //ArmPose p = InverseKinematics.solveForTheta(armPoseXZ); // todo: do we get the same thing back?
        return armPoseXZ;
    }

    // to point down in front is tilt=180, so if th1=-90, th2=180, yields th3=90
    // to point down in back is tilt=-180, so if th1=-90, th2=0, yields th3=-90
    public double getTh3(double th1, double th2){
        if(useTilt){
            return tilt - th1 - th2;
        }
        return th3;
    }

    // changes the target x, z into a target for the third joint, by subtracting the third segment vector at the given tilt
    public Vector2D getJ3Target(){
        double dx = ArmConstants.L3 * Math.sin(tilt);
        double dz = ArmConstants.L3 * Math.cos(tilt);
        return new Vector2D(x - dx, z - dz);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("(x,z)=[");
        sb.append(x);
        sb.append(", ");
        sb.append(z);
        if(useTilt){
            sb.append("] (th3,th4,tilt)=[");
            sb.append(th3 + ", ");
            sb.append(th4 + ", ");
            sb.append(tilt + "]");
        }
        else {
            sb.append("] (th3,th4,tilt)=[");
            sb.append(th3 + ", ");
            sb.append(th4 + "]");
        }
        return sb.toString();
    }

    // This function outputs a report string of static elements of the ArmPoseXZ class
    public static String report(){
        StringBuilder sb = new StringBuilder();
        sb.append("home=" + home().toString());
        sb.append("\nready=" + ready().toString());
        return sb.toString();
    }
}
