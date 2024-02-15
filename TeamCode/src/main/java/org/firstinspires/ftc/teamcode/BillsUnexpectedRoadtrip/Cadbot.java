package org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmController;
import org.firstinspires.ftc.teamcode.BillsEs.AllianceColor;
import org.firstinspires.ftc.teamcode.BillsEs.AlliancePosition;
import org.firstinspires.ftc.teamcode.BillsEs.ControlType;
import org.firstinspires.ftc.teamcode.BillsTensorTunes.SpikeMark;
import org.firstinspires.ftc.teamcode.BillsTensorTunes.ThirdEyeSurfer;
import org.firstinspires.ftc.teamcode.sequencer.AutoPilot;

public class Cadbot {
    public MotorPool motorPool;
    public DeadWheelTracker deadWheelTracker;
    public Telemetry telemetry;

    public ArmController armController;

    public MecanumController mecanumController;
    public HardwareMap hardwareMap;
    public GamePadController gamePadController;
    public GamePadController2 gamePadController2;

    public AutoPilot autoPilot;

    public AllianceColor allianceColor = AllianceColor.RED;
    public AlliancePosition alliancePosition = AlliancePosition.LEFT;
    public SpikeMark spikeMark = SpikeMark.UNKNOWN;
    public ControlType controlType = ControlType.DRIVER_ASSISTED;

    public ThirdEyeSurfer thirdEyeSurfer;

    private boolean runningAutonomous = true;

    // forward offset is the distance from the back of the robot to its center
    public final static double FORWARD_TO_CENTER_OF_ROBOT = 8.5; // inches

    public Cadbot(boolean runningAutonomous){
        this.runningAutonomous = runningAutonomous;
    }

    public boolean isRunningAutonomous(){
        return runningAutonomous;
    }

    public void initialize(HardwareMap hardwareMap, Telemetry telemetry, Gamepad gamepad1, Gamepad gamepad2){
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;

        motorPool = new MotorPool();
        motorPool.initialize(hardwareMap);

        deadWheelTracker = new DeadWheelTracker();
        deadWheelTracker.initialize(this);

        mecanumController = new MecanumController(motorPool);

        armController = new ArmController(this);

        gamePadController = new GamePadController();
        gamePadController.initialize(gamepad1, gamepad2, this);

        gamePadController2 = new GamePadController2();
        gamePadController2.initialize(gamepad1, gamepad2, this);

        autoPilot = new AutoPilot();
        autoPilot.initialize(this);

        thirdEyeSurfer = new ThirdEyeSurfer();
        thirdEyeSurfer.initialize(this);
    }

    // this function is called for driver mode
    public void update(){
        // clear cache to get ready for a bulk motor read
        motorPool.ready();

        // update knowledge of position, heading, velocity, acceleration
        deadWheelTracker.update();

        // run the gamepad controller to perform driving
        gamePadController.update();
        gamePadController2.update();

        // update automated sequences
        autoPilot.update();

        telemetry.update();
    }

    public void quit(){
        // Save more CPU resources when camera is no longer needed.
        thirdEyeSurfer.close();
    }

}
