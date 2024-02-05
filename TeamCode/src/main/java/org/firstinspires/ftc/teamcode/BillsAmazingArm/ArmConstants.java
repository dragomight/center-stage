package org.firstinspires.ftc.teamcode.BillsAmazingArm;

public class ArmConstants {
    public final static double L0z = 7.75;
    public final static double L0x = 1.875; // joint 0 is imaginary and immovable, located on the floor at robot center
    public final static double L0 = Math.sqrt(L0z*L0z + L0x*L0x); // in  the distance from robot center on floor to first joint
    public final static double L1 = 9.5; // in   the distance from joint 1 to joint 2
    public final static double L2 = 12.3125; // in   the distance from joint 2 to joint 3
    public final static double L3 = 8.4; // 7.0+5.0/8.0; // in   the distance from joint 3 to the pulley center

    public final static double TH0 = Math.PI - Math.atan2(L0z, L0x);
    public final static double TH1MIN = Math.toRadians(-90.0); // base joint angle
    public final static double TH1MAX = Math.toRadians(90.0);
    public final static double TH1HOME = Math.toRadians(-90.0);
    public final static double TH2MIN = Math.toRadians(-180.0); // elbow joint angle
    public final static double TH2MAX = Math.toRadians(180.0);
    public final static double TH2HOME = Math.toRadians(180.0);
    public final static double TH3MIN = Math.toRadians(-45.0); // the servo motor or wrist joint
    public final static double TH3MAX = Math.toRadians(90.0);
    public final static double TH3HOME = Math.toRadians(90.0);
    public final static double TH4MIN = Math.toRadians(-90.0); // the roll joint
    public final static double TH4MAX = Math.toRadians(90.0);
    public final static double TH4HOME = Math.toRadians(0.0);

    // MOTOR CONSTANTS
    public static final double TICK_PER_REV = 5281.1;
    public static final double TICKS_PER_DEGREE = TICK_PER_REV/360.0;
    public static final double DEGREES_PER_TICK = 360.0/TICK_PER_REV;
    public final static double RADIANS_PER_TICK = 2.0*Math.PI/TICK_PER_REV;

    // CONTROL CONSTANTS
    public final static double DISTANCE_THRESHOLD = 1.0; // inches, we need to tune this down to fine size.
    public final static double VMAX = 12; // in/s
    public final static double AMAX = 72; // in/s2  288 allows a stopping distance of 1 in when moving 24 in/s
                                            // 72 in/s2 has a stopping distance of 1 in when moving 12 in/s
}
