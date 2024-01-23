package org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip;

import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.MotorPool;

public class MecanumController {

    MotorPool motorPool;
    private final static double MAX_SAFE_POWER = 1.0;

    public MecanumController(MotorPool motorPool){
        this.motorPool = motorPool;
    }

    // Transform from desired motions relative to the field, to motor power settings:
    // This process is complicated by the choice of starting position and side (red or blue)
    public void setDrivePowerRelativeToField(double axial, double lateral, double yaw){
        //
        //setDrivePowerRelativeToRobot(ax, lat, yaw);
    }

    // Transform from desired motions relative to the robot, to motor power settings:
    // axial is forward and backward for the robot
    // lateral is left right (strafing) for the robot
    // yaw is the amount to turn
    public void setDrivePowerRelativeToRobot(double axial, double lateral, double yaw){
        double max;
        //lateral *= -1; // we have to reverse it to match robot coordinates

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        double leftFrontPower  = axial + lateral + yaw;
        double rightFrontPower = axial - lateral - yaw;
        double leftBackPower   = axial - lateral + yaw;
        double rightBackPower  = axial + lateral - yaw;

        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.
        max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        max /= MAX_SAFE_POWER; // todo: remove this when done with testing

        if (max > 1.0) {
            leftFrontPower  /= max;
            rightFrontPower /= max;
            leftBackPower   /= max;
            rightBackPower  /= max;
        }

        // set the power to the motors
        motorPool.setDrivePower(leftFrontPower, rightFrontPower, rightBackPower, leftBackPower);

    }
}
