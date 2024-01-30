package org.firstinspires.ftc.teamcode.BillsAmazingArm;

import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;

/**
 * Transforms from joint angles to the end position of the arm, for three angles and lengths
 * with the result in robot coordinates.
 * Robot coordinates:
 * X is forward
 * Y is left
 * Z is up
 */
public class Kinematics {
    // takes the ArmPose and
    // returns the end point joint 3 of a robotic arm in robot coords
    public static Vector2D j3(ArmPose pose){
        double x = 0;
        double z = 0;

        x = ArmConstants.L0x + ArmConstants.L1 * Math.sin(pose.th1) + ArmConstants.L2 * Math.sin(pose.th1 + pose.th2);
        z = ArmConstants.L0z + ArmConstants.L1 * Math.cos(pose.th1) + ArmConstants.L2 * Math.cos(pose.th1 + pose.th2);

        return new Vector2D(x, z);
    }

    public static Vector2D tip(ArmPose pose){
        double x = 0;
        double z = 0;

        x = ArmConstants.L0x + ArmConstants.L1 * Math.sin(pose.th1) + ArmConstants.L2 * Math.sin(pose.th1 + pose.th2) + ArmConstants.L3 * Math.sin(pose.th1 + pose.th2 + pose.th3);
        z = ArmConstants.L0z + ArmConstants.L1 * Math.cos(pose.th1) + ArmConstants.L2 * Math.cos(pose.th1 + pose.th2) + ArmConstants.L3 * Math.cos(pose.th1 + pose.th2 + pose.th3);

        return new Vector2D(x, z);
    }
}
