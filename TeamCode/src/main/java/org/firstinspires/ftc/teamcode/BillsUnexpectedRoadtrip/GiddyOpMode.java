package org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip;

import android.app.Application;
import android.util.Log;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.BillsEs.AllianceColor;
import org.firstinspires.ftc.teamcode.BillsEs.AlliancePosition;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@Autonomous(name="GiddyOp", group = "Concept")

public class GiddyOpMode extends LinearOpMode{

    public static final File DRAGOMIGHT_FOLDER =
            new File(AppUtil.ROOT_FOLDER + "/Dragomight/");

    public Cadbot cadbot = new Cadbot();

    @Override public void runOpMode(){

        boolean done = false;

        telemetry.addData(">", "Select Driver Options...");
        telemetry.addData(">", "BLUE <X> or RED <B>?");
        telemetry.update();

        while(!done && !isStopRequested()) {
            while(gamepad1.x){
                cadbot.allianceColor = AllianceColor.BLUE;
                done = true;
            }
            while(gamepad1.b){
                cadbot.allianceColor = AllianceColor.RED;
                done = true;
            }
        }
        done = false;
        telemetry.addData(">", "Color is " + cadbot.allianceColor);
        telemetry.addData(">", "Select Alliance Position:");
        telemetry.addData(">", "LEFT <X> or RIGHT <B>");
        telemetry.update();

        while(!done && !isStopRequested()) {
            while(gamepad1.x){
                cadbot.alliancePosition = AlliancePosition.LEFT;
                done = true;
            }
            while(gamepad1.b){
                cadbot.alliancePosition = AlliancePosition.RIGHT;
                done = true;
            }
        }

        telemetry.addData(">", "Color is " + cadbot.allianceColor);
        telemetry.addData(">", "Position is " + cadbot.alliancePosition);
        telemetry.update();

        cadbot.initialize(hardwareMap, telemetry, gamepad1, gamepad2);
        //writeToFile(cadbot.deadWheelTracker.getPose());

        waitForStart();

        while (!isStopRequested()) {
            cadbot.autoUpdate();
        }

        // todo: store the current field pose for the driver op mode
//        writeToFile(cadbot.deadWheelTracker.getPose());
        cadbot.quit();
    }

    public void writeToFile(Vector2D1 v){
        try {
            File myFile = new File(DRAGOMIGHT_FOLDER, "LastPose.txt");
            FileWriter writer = new FileWriter(myFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            Double x = v.getX();
            bufferedWriter.write(x.toString());
            bufferedWriter.newLine();

            Double y = v.getY();
            bufferedWriter.write(y.toString());
            bufferedWriter.newLine();

            Double h = v.getHeading();
            bufferedWriter.write(h.toString());

            bufferedWriter.close();
        }
        catch (IOException e) {
            Log.e("GiddyOpMode", e.toString());
            e.printStackTrace();
        }
        finally {

        }
    }

}
