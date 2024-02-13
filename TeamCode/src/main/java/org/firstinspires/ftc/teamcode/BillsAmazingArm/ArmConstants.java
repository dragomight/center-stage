package org.firstinspires.ftc.teamcode.BillsAmazingArm;

import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UtilityKit;

public class ArmConstants {
    public final static double L0z = 7.75;
    public final static double L0x = 1.875; // joint 0 is imaginary and immovable, located on the floor at robot center
    public final static double L0 = Math.sqrt(L0z*L0z + L0x*L0x); // in  the distance from robot center on floor to first joint
    public final static double L1 = UtilityKit.mmToIn(10.0 * 24.0); // 9.5 - 1.0/8.0; // in   the distance from joint 1 to joint 2
    public final static double L2 = UtilityKit.mmToIn(13.0 * 24.0);  // in   the distance from joint 2 to joint 3 (measure the difference between joint 1 and 3 to confirm)
    public final static double L3 = 8.5; // in from the joint to the tip

    public final static double TH0 = Math.PI - Math.atan2(L0z, L0x);
    public final static double TH1MIN = Math.toRadians(-90.0); // base joint angle
    public final static double TH1MAX = Math.toRadians(90.0);
    public final static double TH1HOME = Math.toRadians(-90.0);

    public final static double TH2MIN = Math.toRadians(-180.0); // elbow joint angle
    public final static double TH2MAX = Math.toRadians(180.0);
    public final static double TH2HOME = Math.toRadians(180) - Math.asin((L3-L0z)/L2); // based on the difference in height of the two joints

    public final static double TH3MIN = Math.toRadians(-60.0); // the servo motor or wrist joint
    public final static double TH3MAX = Math.toRadians(135.0);
    public final static double TH3HOME = Math.toRadians(90.0);

    public final static double TH4MIN = Math.toRadians(-90.0); // the roll joint
    public final static double TH4MAX = Math.toRadians(90.0);
    public final static double TH4HOME = Math.toRadians(0.0);

    // BACKLASH CONSTANTS
    public final static double CM1 = L1*2.0/3.0; // in, length from joint to center of mass of the segment 1
    public final static double CM2 = L2/2.0;
    public final static double CM3 = L3/2.0;
    public final static double M1 = 98.0*2      // 11 Hole U-channel x2
                                    + 524       // High torque DC motor
                                    + 15.0*2    // Pattern mount x2
                                    + 23*2      // Bevel gear x2
                                    + 22        // Axle
                                    + 14;       // Sonic Hub
    public final static double M2 = 50.0        // 5 Hole C-channel
                                    + 35        // 1 Hole U-channel
                                    + 114       // 13 Hole Low Side U-channel
                                    + 50        // hook, just guessing
                                    + 15*2;     // Pattern mount x2
    public final static double M3 =  35 + 44    // Light Servo block x2
                                    + 60*2      // Servo motor x4
                                    + 21*2      // 3x7 grid plate x2
                                    + 20;       // printed gripper, belts
    public final static double BACKLASH1 = Math.toRadians(16.7); // backlash of joint 1
    public final static double BACKLASH2 = Math.toRadians(-17.0); // backlash of joint 2

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

    public static String string(){
        StringBuilder sb = new StringBuilder();
        sb.append("L1=" + L1);
        sb.append("  L2=" + L2);
        sb.append("  TH2Home=" + Math.toDegrees(TH2HOME));
        return sb.toString();
    }
}
