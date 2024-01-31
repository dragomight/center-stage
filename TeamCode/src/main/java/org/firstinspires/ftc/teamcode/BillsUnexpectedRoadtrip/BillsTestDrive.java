package org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip;

import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BillsEs.AllianceColor;
import org.firstinspires.ftc.teamcode.BillsEs.AlliancePosition;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D1;

import java.io.File;
import java.util.Scanner;

@TeleOp(name="Bills Test Drive", group="Bill's Emporium")
public class BillsTestDrive extends LinearOpMode {
    public Cadbot cadbot = new Cadbot();

    @Override public void runOpMode(){
        boolean done = false;

        telemetry.addData(">", "Select Driver Options...");
        telemetry.addData(">", "BLUE <X> or RED <B>?");
        telemetry.update();

        cadbot.alliancePosition = AlliancePosition.RIGHT;

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

        // read last pose from file and reset dead wheel tracker
        Vector2D1 v = readFromFileUsingScanner();
        cadbot.deadWheelTracker.resetPose(v);

        waitForStart();

        while (!isStopRequested()) {
            cadbot.update();
        }

        cadbot.quit();
    }

    public Vector2D1 readFromFileUsingScanner() {
        double[] d = new double[3];
        int i=0;
        try {
            // pass the path to the file as a parameter
            File file = new File(GiddyOpMode.DRAGOMIGHT_FOLDER, "LastPose.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                d[i++] = Double.parseDouble(sc.nextLine());
            }
        }
        catch (Exception e){
            System.out.println(e);
            d[0]=0;
            d[1]=0;
            d[2]=0;
            Log.e("BillsTestDrive", e.toString());
        }
        return new Vector2D1(d[0], d[1], d[2]);
    }

}
