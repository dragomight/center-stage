package org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip;

import com.acmerobotics.roadrunner.geometry.Pose2d;

public class DeadWheelTracker {

    // Dead wheel data
    public static double TICKS_PER_REV = 2000;
    public static double WHEEL_RADIUS = 0.9448819; // in                24mm = 0.9448819 in
    public static double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed
    public static double LATERAL_DISTANCE = 7.25; // in; distance between the left and right wheels
    public static double FORWARD_OFFSET = -3.75; // in; offset of the lateral wheel (negative is toward the back)
    public static double INCHES_PER_TICK = 2*Math.PI*WHEEL_RADIUS/TICKS_PER_REV;

    private double bearing = 0; // robot's bearing in radians
    private double xWorld = 0; // the robot's position in world coordinates, in inches
    private double yWorld = 0;

    // Last wheel positions
    int lfOld;
    int rfOld;
    int rbOld;

    MotorPool motorPool;

    public void initialize(MotorPool motorPool){
        this.motorPool = motorPool;
    }

    public Pose2d getPoseEstimate(){

        int lfp = -motorPool.getLeftFrontTicks();   // Uses 1 bulk-read to obtain ALL the motor data
        int rfp = -motorPool.getRightFrontTicks();   // There is no penalty for doing more `get` operations in this cycle,
        int rbp = -motorPool.getRightBackTicks();

//        double lfv = leftFrontDrive.getVelocity();  // ticks per second
//        double rfv = rightFrontDrive.getVelocity();
//        double lbv = leftBackDrive.getVelocity();
//        double rbv = rightBackDrive.getVelocity();

        // difference from last time
        double dlf = lfp - lfOld;
        double drf = rfp - rfOld;
        //double dlb = lbp - lbOld;
        double drb = rbp - rbOld;

        // store the current value
        lfOld = lfp;
        rfOld = rfp;
//        lbOld = lbp;
        rbOld = rbp;

        // convert from ticks to inches
        double e1 = INCHES_PER_TICK * dlf;
        double e2 = INCHES_PER_TICK * drf;
        double e3 = INCHES_PER_TICK * drb;

        // update robot motion
        double dTheta = (e2 - e1)/LATERAL_DISTANCE;
        double dx = (e1 + e2)/2;  // forward motion
        double dy = e3 + FORWARD_OFFSET * dTheta;

        // transform motion to world coordinates and update world coordinates
        bearing += dTheta;
        double dxWorld = dx * Math.cos(bearing) - dy * Math.sin(bearing);
        double dyWorld = dy * Math.cos(bearing) + dx * Math.sin(bearing);
        xWorld += dxWorld;
        yWorld += dyWorld;

        // form as a pose
        Double x = xWorld;
        Double y = yWorld;
        Double heading = Math.toDegrees(bearing);

        return new Pose2d(x, y, heading);
    }
}
