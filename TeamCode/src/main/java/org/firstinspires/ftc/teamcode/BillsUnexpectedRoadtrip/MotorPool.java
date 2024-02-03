package org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip;

import android.util.Log;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.apache.commons.math3.util.IterationListener;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;
import org.firstinspires.ftc.teamcode.BillsYarm.Yoint;

import java.util.List;

public class MotorPool {
    List<LynxModule> allHubs; // reference to the control hubs

    private DcMotorEx leftFrontDrive   = null;  //  Used to control the left front drive wheel
    private DcMotorEx rightFrontDrive  = null;  //  Used to control the right front drive wheel
    private DcMotorEx leftBackDrive    = null;  //  Used to control the left back drive wheel
    private DcMotorEx rightBackDrive   = null;  //  Used to control the right back drive wheel

    // Declare joint controllers for balustrade
    public DcMotorEx joint1;
    public DcMotorEx joint2;

    // Declare balustrade servos
    public Servo launchMan;
    public Servo tiltMan;

    // Gripper servos
    private Servo rock; // wrist pitch
    private Servo roll; // wrist roll
    private CRServo gripperTop; // super speed servos
    private CRServo gripperBottom;

    // Functionality Limits
    public boolean gripperOnline = true;

    public void initialize(HardwareMap hardwareMap){

        try {


            // Initialize the hardware variables. Note that the strings used here as parameters
            // to 'get' must match the names assigned during the robot configuration.
            // step (using the FTC Robot Controller app on the phone).
            leftFrontDrive = hardwareMap.get(DcMotorEx.class, "FrontLeft");
            rightFrontDrive = hardwareMap.get(DcMotorEx.class, "FrontRight");
            leftBackDrive = hardwareMap.get(DcMotorEx.class, "BackLeft");
            rightBackDrive = hardwareMap.get(DcMotorEx.class, "BackRight");

            // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
            // When run, this OpMode should start both motors driving forward. So adjust these two lines based on your first test drive.
            // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
            leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
            leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
            rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
            rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

            leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        catch (Exception e){
            Log.e("MotorPool", "Drivetrain and rotary encoders failed to initialize.");
            Log.e("MotorPool", e.toString());
        }
        try {
            // Initialize arm dc motors
            joint1 = hardwareMap.get(DcMotorEx.class, "Joint1");
            joint2 = hardwareMap.get(DcMotorEx.class, "Joint2");

            joint1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            joint2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            joint1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            joint2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            joint2.setDirection(DcMotorSimple.Direction.REVERSE);

//            joint1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
//            joint2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            joint1.setPower(0.0);
            joint2.setPower(0.0);
        }
        catch (Exception e) {
            Log.e("MotorPool: initialize", "Arm dc motors failed to initialize");
            Log.e("MotorPool: initialize", e.toString());
        }

        try {
            launchMan = hardwareMap.get(Servo.class, "Launcher");
            launchMan.setDirection(Servo.Direction.REVERSE);
            tiltMan = hardwareMap.get(Servo.class, "Tilter");

        }
        catch (Exception e) {
            System.out.println("Servos failed to initialize");
            Log.e("Actuators", "Servo motors failed to initialize");
            Log.e("Actuators", e.toString());
        }

        try{
            gripperBottom = hardwareMap.get(CRServo.class, "GripA");
            gripperTop = hardwareMap.get(CRServo.class, "GripB");
            rock = hardwareMap.get(Servo.class, "Rock");
            roll = hardwareMap.get(Servo.class, "Roll");

        }
        catch (Exception e) {
            Log.e("MotorPool", "Gripper servos failed to initialize");
            Log.e("MotorPool", e.toString());
            gripperOnline = false;
        }

        // set to bulk motor read with manual clearing of the cache
        allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule module : allHubs) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

    }

    public void ready(){
        // Important Step: If you are using MANUAL mode, you must clear the BulkCache once per control cycle
        for (LynxModule module : allHubs) {
            module.clearBulkCache();
        }
    }

    // THREE DEAD WHEEL ODOMETRY READERS
    public int getLeftFrontTicks(){
        return -leftFrontDrive.getCurrentPosition();
    }

    public int getRightFrontTicks(){
        return -rightFrontDrive.getCurrentPosition();
    }

    public int getRightBackTicks(){
        return -rightBackDrive.getCurrentPosition();
    }

    // TWO ARM JOINT READERS

    public int getJoint1Ticks(){
        return joint1.getCurrentPosition();
    }

    public int getJoint2Ticks(){
        return joint2.getCurrentPosition();
    }

    public double getJoint1TicksPerSecond(){
        return joint1.getVelocity();
    }

    public double getJoint2TicksPerSecond(){
        return joint2.getVelocity();
    }

    public void setJoint1TicksPerSecond(double tps){
        joint1.setVelocity(tps);
    }

    public void setJoint2TicksPerSecond(double tps){
        joint2.setVelocity(tps);
    }

    public void setRockJointPosition(double pos){
        if(gripperOnline)
            rock.setPosition(pos);
    }

    public void setJoint1Position(int ticks){
        joint1.setTargetPosition(ticks);
    }

    public void setJoint2Position(int ticks){
        joint2.setTargetPosition(ticks);
    }

    public void setRollJointPosition(double pos){
        if(gripperOnline)
            roll.setPosition(pos);
    }

    public double getRockJointPosition(){
        if(gripperOnline)
            return rock.getPosition();
        else
            return 0.0;
    }

    // this will just return the value you set it to, not something detected
    public double getRollJointPosition(){
        if(gripperOnline)
            return roll.getPosition();
        else
            return 0.0;
    }

    public void setGripperStop(){
        if(gripperOnline) {
            gripperTop.setDirection(DcMotorSimple.Direction.FORWARD);
            gripperTop.setPower(0.0);
            gripperBottom.setDirection(DcMotorSimple.Direction.REVERSE);
            gripperBottom.setPower(0.0);
        }
        Log.e("MotorPool", "setGripperStop");
    }

    public void setGripperPush(){
        if(gripperOnline) {
            gripperTop.setDirection(DcMotorSimple.Direction.FORWARD);
            gripperTop.setPower(1.0);
            gripperBottom.setDirection(DcMotorSimple.Direction.REVERSE);
            gripperBottom.setPower(1.0);
        }
        Log.e("MotorPool", "setGripperPush");
    }

    public void setGripperPull(){
        if(gripperOnline){
            gripperTop.setDirection(DcMotorSimple.Direction.REVERSE);
            gripperTop.setPower(1.0);
            gripperBottom.setDirection(DcMotorSimple.Direction.FORWARD);
            gripperBottom.setPower(1.0);
        }
        Log.e("MotorPool", "setGripperPull");
    }

    public void setDrivePower(double leftFront, double rightFront, double rightBack, double leftBack){
        leftFrontDrive.setPower(leftFront);
        rightFrontDrive.setPower(rightFront);
        rightBackDrive.setPower(rightBack);
        leftBackDrive.setPower(leftBack);
    }

    public void update(Cadbot cadbot) {

        // READ ALL DC MOTORS
//        cadbot.yarm.joint1.updateTicks(joint1.getCurrentPosition());
//        cadbot.yarm.joint2.updateTicks(joint2.getCurrentPosition());
//        cadbot.yarm.joint1.updateTicksPerSecond(joint1.getVelocity());
//        cadbot.yarm.joint2.updateTicksPerSecond(joint2.getVelocity());
        //cadilac.yarm.calculateEndpoint();

        //UPDOOT
        //cadilac.cinnamonController.update(verbose);
        //cadbot.yarmController.update(true);

        // SET THE DC MOTORS AND SERVOS
        if (cadbot.yarm.launch) {
            launchMan.setPosition(0.5);
        }
        else {
            launchMan.setPosition(0);
        }
        if (cadbot.yarm.tilt) {
            tiltMan.setPosition(60/300.0);
        }
        else {
            tiltMan.setPosition(0);
        }

//        cadbot.telemetry.addData("JT1 ", joint1.getCurrentPosition());
//        cadbot.telemetry.addData("JT2 ", joint2.getCurrentPosition());
//
//        joint1.setTargetPosition(cadbot.yarm.joint1TickTarget);
//        joint2.setTargetPosition(cadbot.yarm.joint2TickTarget);
//
//        joint1.setPower(1);
//        joint2.setPower(1);
//
//        joint1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
//        joint2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
}
