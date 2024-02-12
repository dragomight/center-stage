package org.firstinspires.ftc.teamcode.BillsAmazingArm;

import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;

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

    // Calculates the amount of correction to the angles of the first two joints
    // todo: calculations here are not efficient and could be improved or simplified
    public static Vector2D getCorrection(ArmPose pose){
        Vector2D correction = new Vector2D();

        // position of center of mass of segment 3
        Vector2D v3 = Kinematics.cm3(pose);

        // position of center of mass of segment 2
        Vector2D v2 = Kinematics.cm2(pose);

        // position of center of mass of segment 1
        Vector2D v1 = Kinematics.cm1(pose);

        // calculate the weighted average cm for joint 2
        Vector2D v23 = Vector2D.weightedAverage(v2, ArmConstants.M2, v3, ArmConstants.M3);

        // get joint 2 position
        Vector2D j2 = Kinematics.j2(pose);

        // if the cm is behind joint 2, add the correction
        if(v23.getX() - j2.getX() < 0){
            correction.setY(ArmConstants.BACKLASH2);
        }

        // calculate the weighted average cm for all segments
        Vector2D v123 = Vector2D.weightedAverage(v1, ArmConstants.M1, v23, ArmConstants.M2 + ArmConstants.M3); // the weight is already applied

        // if the cm is behind joint 1, do not add the correction
        if(v123.getX() - ArmConstants.L0x > 0){
            correction.setX(ArmConstants.BACKLASH1);
        }

        return correction;
    }
}
