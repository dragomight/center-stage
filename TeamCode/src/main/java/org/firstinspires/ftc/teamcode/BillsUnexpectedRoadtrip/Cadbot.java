package org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BillsEs.AllianceColor;
import org.firstinspires.ftc.teamcode.BillsEs.AlliancePosition;
import org.firstinspires.ftc.teamcode.BillsEs.ControlType;
import org.firstinspires.ftc.teamcode.BillsSystemsForSpareChange.GamePadState;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D1;
import org.firstinspires.ftc.teamcode.BillsYarm.Yarm;
import org.firstinspires.ftc.teamcode.BillsYarm.YarmController;
import org.firstinspires.ftc.teamcode.sequence.AutoPilot;
import org.firstinspires.ftc.teamcode.sequence.GameField;

public class Cadbot {
    public MotorPool motorPool;
    public DeadWheelTracker deadWheelTracker;
    public Telemetry telemetry;

    public MecanumController mecanumController;
    public HardwareMap hardwareMap;
    public GamePadController gamePadController;

    public GameField gameField;
    public AutoPilot autoPilot;

    public Yarm yarm;
    public YarmController yarmController;
    public GamePadState gamePadState1; // only used for yarm
    public Gamepad gamepad1;

    public AllianceColor allianceColor = AllianceColor.RED;
    public AlliancePosition alliancePosition = AlliancePosition.LEFT;
    public ControlType controlType = ControlType.DRIVER_ASSISTED;

    // forward offset is the distance from the back of the robot to its center
    public final static double FORWARD_TO_CENTER_OF_ROBOT = 8.5; // inches

    public void initialize(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2){
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
        this.gamepad1 = gamepad1;

        motorPool = new MotorPool();
        motorPool.initialize(hardwareMap);

        gameField = new GameField(allianceColor, alliancePosition, FORWARD_TO_CENTER_OF_ROBOT);

        deadWheelTracker = new DeadWheelTracker();
        deadWheelTracker.initialize(motorPool, gameField);

        mecanumController = new MecanumController(motorPool);

        gamePadController = new GamePadController();
        gamePadController.initialize(gamepad1, gamepad2, this);

        autoPilot = new AutoPilot();
        autoPilot.initialize(this);

        gamePadState1 = new GamePadState(); // only used for yarm
        gamePadState1.initialize(telemetry);

        yarm = new Yarm();
        yarmController = new YarmController();
        yarmController.initialize(gamePadState1, yarm, telemetry);
    }

    // this function is called for driver mode
    public void update(){
        // clear cache to get ready for a bulk motor read
        motorPool.ready();
        // update knowledge of position, heading, velocity, acceleration
        deadWheelTracker.update();
        // temp: get pose for testing
        Vector2D1 poseEstimate = deadWheelTracker.getPose();

        // run the gamepad controller to perform driving
        gamePadController.update();

        // update the arm
        gamePadState1.update(gamepad1, false);
        motorPool.update(this);

        // todo: change telemetry
        telemetry.addData("x", poseEstimate.getX());
        telemetry.addData("y", poseEstimate.getY());
        telemetry.addData("heading", Math.toDegrees(poseEstimate.getHeading()));
        telemetry.update();
    }

    // This function is called for autonomous mode
    public void autoUpdate(){
        // clear cache to get ready for a bulk motor read
        motorPool.ready();
        // update knowledge of position, heading, velocity, acceleration
        deadWheelTracker.update();
        // temp: get pose for testing
        Vector2D1 poseEstimate = deadWheelTracker.getPose();

        // perform autonomous sequence
        autoPilot.update();

        // todo: change telemetry
        telemetry.addData("x", poseEstimate.getX());
        telemetry.addData("y", poseEstimate.getY());
        telemetry.addData("heading", Math.toDegrees(poseEstimate.getHeading()));
        telemetry.update();
    }

}
