package org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip;

import android.util.Log;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmPose;
import org.firstinspires.ftc.teamcode.BillsEs.AllianceColor;
import org.firstinspires.ftc.teamcode.BillsEs.ControlType;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UtilityKit;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D1;
import org.firstinspires.ftc.teamcode.sequencer.engine.GameField;

public class GamePadController {
    private MecanumController mecanumController;
    private Gamepad gamepad1;
    private Gamepad gamepad2;
    private Cadbot cadbot;

    private double preferredHeading;
    private double error;
    private double maxError;
    private boolean updateHeading = true;
    private final static double KP = 1.0; // the proportionate gain for pid control

    public void initialize(Gamepad gamepad1, Gamepad gamepad2, Cadbot cadbot){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.mecanumController = cadbot.mecanumController;
        this.cadbot = cadbot;
    }

    public void update(){

        if(cadbot.controlType == ControlType.STANDARD) {
            // read the joysticks: POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            // talk to the mecanum controller
            mecanumController.setDrivePowerRelativeToRobot(axial, lateral, yaw);
        }
        // or Driver assisted mode
        else{
            // read the joysticks
            double axial = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            // use joystick to indicate field coordinates
            double x, y;
            if(cadbot.allianceColor == AllianceColor.RED){
                x = lateral;
                y = -axial;
            }
            else{
                x = -lateral;
                y = axial;
            }

            // get the most recent pose
            Vector2D1 currentPose = cadbot.deadWheelTracker.getPose();

            // transform that vector to robot coordinates
            Vector2D toTargetRobotCoords = GameField.fieldToRobot(new Vector2D(x, y), -currentPose.getHeading());

            // if yaw is zero, attempt to maintain the preferred heading
            if(yaw == 0) {
                if(updateHeading){
                    updateHeading = false;
                    preferredHeading = currentPose.getHeading();
                }
                else {
                    // set the value of the yaw using a PID controller
                    error = -Vector2D1.angularDifference(preferredHeading, currentPose.getHeading())/Math.PI;
                    yaw = error * KP; // error is already small
                    yaw = UtilityKit.limitToRange(yaw, -1.0, 1.0);
                    if(Math.abs(error) > maxError){
                        maxError = Math.abs(error);
                    }
                }
            }
            else {
                updateHeading = true;
            }

            // dispatch the instructions to the mecanum controller
            mecanumController.setDrivePowerRelativeToRobot(toTargetRobotCoords.getX(), toTargetRobotCoords.getY(), yaw);

            cadbot.telemetry.addData("maxErr", maxError);
            cadbot.telemetry.addData("fx", x);
            cadbot.telemetry.addData("fy", y);
            cadbot.telemetry.addData("axial", toTargetRobotCoords.getX());
            cadbot.telemetry.addData("lateral", toTargetRobotCoords.getY());

            cadbot.deadWheelTracker.recordsUpdate();
            Log.e("GamePadController", cadbot.deadWheelTracker.toString());
            Log.e("GamePadController", "power=" + toTargetRobotCoords.getX() + "   " + toTargetRobotCoords.getY() + "   " + yaw);
            Log.e("GamePadController", "topSpeed=" + cadbot.deadWheelTracker.topSpeed + "   topAccel=" + cadbot.deadWheelTracker.topAcceleration);
        }

        // Drone Launcher
//        if (gamepad1.right_trigger > 0.1) {
//            cadbot.yarm.tilt = true;
//        }
//        else {
//            cadbot.yarm.tilt = false;
//        }
//        cadbot.yarm.launch = gamepad1.y;
//
//        //cadbot.telemetry.addData("Tilt ", cadbot.yarm.tilt);
//        //cadbot.telemetry.addData("Launch ", cadbot.yarm.launch);
//
//        if (gamepad1.dpad_up) { // Ready position
//            cadbot.yarm.joint1TickTarget = Yoint.degreesToTicks(-65);
//            cadbot.yarm.joint2TickTarget = Yoint.degreesToTicks(-170);
//        }
//
//        if (gamepad1.dpad_right) { // Pull
//            cadbot.yarm.joint1TickTarget = Yoint.degreesToTicks(60);
//            cadbot.yarm.joint2TickTarget = Yoint.degreesToTicks(-90);
//        }
//
//        if (gamepad1.dpad_down) { // Fold
//            cadbot.yarm.joint1TickTarget = Yoint.degreesToTicks(5);
//            cadbot.yarm.joint2TickTarget = Yoint.degreesToTicks(-5);
//        }
//
//        cadbot.telemetry.addData("Joint1 ", cadbot.yarm.joint1TickTarget);
//        cadbot.telemetry.addData("Joint2 ", cadbot.yarm.joint2TickTarget);

        if (gamepad1.back) { // Reset orientation
            cadbot.deadWheelTracker.resetPose(new Vector2D1(0, 0, 0));
        }

        /////////////////////////////////////////////////////////////////////////////////////////
        // Manual Mode Arm Controls for the Truly Desperate
        ArmPose p = cadbot.armController.getTargetPose();
        double dTh1 = 0;
        double dTh2 = 0;
        double ink = Math.toRadians(1);; // degrees

        if(gamepad1.a){
            // increment joint1 position
            dTh1 = ink;
        }
        if(gamepad1.b){
            // increment joint1 position
            dTh1 = -ink;
        }
        if(gamepad1.x){
            // increment joint1 position
            dTh2 = ink;
        }
        if(gamepad1.y){
            // increment joint1 position
            dTh2 = -ink;
        }

        cadbot.armController.setTargetPosition(p.th1 + dTh1, p.th2 + dTh2, 0, 0);
        Log.e("GamePadController", "targetArmPose" + p);
    }
}
