package org.firstinspires.ftc.teamcode.BillsAmazingArm;

/**
 * Backlash is free movement found in the joints of the robot.  There will always be some amount
 * of backlash, no matter what form of transmission is used.  Understanding backlash can help you
 * make corrections that will improve the accuracy of the robot's movement.
 * With a robotic arm, backlash generally occurs when the center of mass of the arm beyond the joint
 * passes over the joint.  Center of mass positions can be calculated using the kinematic model.
 * Start with the position of the joints, then calculate the cm locations.  If any are past the
 * tipping point, their shifting position may cause others to shift.
 */
public class BacklashCorrection {

    public final static double CM1 = 0; // in, length from joint to center of mass of the segment 1
    public final static double CM2 = 0;
    public final static double CM3 = 0;
    public final static double M1 = 0; // kg,
    public final static double M2 = 0;
    public final static double M3 = 0;

    // 
}
