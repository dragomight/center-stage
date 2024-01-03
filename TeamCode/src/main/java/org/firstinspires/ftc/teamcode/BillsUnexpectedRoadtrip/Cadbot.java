package org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Cadbot {
    public MotorPool motorPool;
    public DeadWheelTracker deadWheelTracker;
    public Telemetry telemetry;

    public void initialize(HardwareMap hardwareMap, Telemetry telemetry){
        this.telemetry = telemetry;
        motorPool = new MotorPool();
        motorPool.initialize(hardwareMap);

        deadWheelTracker = new DeadWheelTracker();
        deadWheelTracker.initialize(motorPool);

        telemetry.addData(">", "Press play to start tests");
        telemetry.addData(">", "Test results will update for each access method.");
        telemetry.update();
    }

    public void update(){
        motorPool.ready(); // perform bulk motor read

        Pose2d poseEstimate = deadWheelTracker.getPoseEstimate();
        telemetry.addData("x", poseEstimate.getX());
        telemetry.addData("y", poseEstimate.getY());
        telemetry.addData("heading", poseEstimate.getHeading());
        telemetry.update();
    }
}
