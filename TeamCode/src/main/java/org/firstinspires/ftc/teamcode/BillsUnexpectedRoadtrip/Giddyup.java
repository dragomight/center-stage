package org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.List;

@Autonomous(name="Giddyup", group = "Concept")

public class Giddyup extends LinearOpMode{

    public Cadbot cadbot = new Cadbot();

    @Override public void runOpMode(){

        cadbot.initialize(hardwareMap, telemetry);

        waitForStart();

        while (!isStopRequested()) {
            cadbot.update();
        }
    }


}
