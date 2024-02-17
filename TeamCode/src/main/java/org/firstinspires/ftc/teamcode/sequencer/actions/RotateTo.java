package org.firstinspires.ftc.teamcode.sequencer.actions;
import android.util.Log;

import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UtilityKit;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D1;
import org.firstinspires.ftc.teamcode.fuzzy.FuzzyController2D;
import org.firstinspires.ftc.teamcode.fuzzy.FuzzyPowerController;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.GameField;

/**
 * This simple motion drives straight at the target (x, y) in field coordinates and turns the
 * robot toward that target until it gets close enough to complete the action.
 */
public class RotateTo implements RobotAction{

    // From testing: topSpeed=64.6 but typical topSpeed=50   topAccel=66   topDecel=100
    public static final double DISTANCE_THRESHOLD = 1.0;
    public static final double ANGULAR_THRESHOLD = Math.toRadians(2.0); // the top number is the allowed error in degrees
    public static final double MAXIMUM_ACHIEVABLE_ACCELERATION = 100.0;
    public static final double KP = 1.1;
    public static final double KI = 0.0; // 1 works but is slow, 100 counters the dt=.01 to make it proportional to the velocity
    public static final double KD = 0;//0.001;
    public static final double TOP_SPEED = 64.67; // the fastest we want to go

    FuzzyController2D powerController = FuzzyPowerController.getPowerController();

    private boolean done = false;
    Cadbot cadbot;
    Vector2D1 target; // the position we tell the robot to drive to
    double maxSpeed; // the fastest we want the robot to drive
    double maxAccel; // the fastest we want the robot to accelerate
    double maxDecel; // the fastest we want the robot to brake, a positive number
    private double error;  // sqrt of the difference between the angle and target angle
    private double dError; // derivative of error
    private double iError; // integral of error
    private double lastError;
    private double lastDistance = 999999;

    private double[] controllerInput = new double[2];
    double power; // the amount of power being applied to the motors, ranging [-1, 1]

    public RotateTo(Cadbot cadbot, Vector2D1 target, double maxSpeed, double maxAccel, double maxDecel){
        this.cadbot = cadbot;
        this.target = target;
        this.maxSpeed = maxSpeed;
        this.maxAccel = maxAccel;
        this.maxDecel = maxDecel;
        Log.e("DriveTo", "Creating DriveTo to target: " + target.toString());
    }

    public RotateTo(Cadbot cadbot, Vector2D1 target){
        this.cadbot = cadbot;
        this.target = target;
        Log.e("DriveTo", "Creating DriveTo to target: " + target.toString());
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public void update() {
        // get the current belief state of position (x, y, bearing), in field coordinates
        Vector2D1 position = cadbot.deadWheelTracker.getPose();

        // get the current belief state of velocity and angular velocity, in field coordinates
        Vector2D1 velocity = cadbot.deadWheelTracker.getVelocity();

        // calculate a vector to the target (how far we need to go and in which direction)
        Vector2D toTarget = new Vector2D(target.getX() - position.getX(), target.getY() - position.getY());

        // distance to the target
        double distance = toTarget.magnitude();

        // last time
        double dt = cadbot.deadWheelTracker.getTime();

        // velocity and speed
        Vector2D v = new Vector2D(velocity.getX(), velocity.getY());
        double speed = v.magnitude();

        // DETERMINE POWER LEVEL TO MAINTAIN THE PREFERRED HEADING:
        // set the value of the yaw using a PID controller:
        // first normalize the error so it ranges over -1 to 1
        double angle = -Vector2D1.angularDifference(target.getHeading(), position.getHeading())/Math.PI;
        error = Math.sqrt(Math.abs(angle)) * Math.signum(angle);
        dError = (error - lastError)/dt;
        iError += error * dt;
        double yaw = error * KP + iError * KI - dError * KD; // error is already small
        yaw = UtilityKit.limitToRange(yaw, -1.0, 1.0);
 //       double abs = Math.abs(yaw);
 //       yaw = Math.sqrt(abs) * yaw / abs; // broadens the range of power from linear

        // CHECK IF THE ACTION IS DONE
        // if we are already within threshold tolerance of the target pose (x, y, bearing), set done = true
        if((Math.abs(angle) < ANGULAR_THRESHOLD && velocity.getHeading() == 0)){
            cadbot.mecanumController.setDrivePowerRelativeToRobot(0, 0, 0);
            done = true;
            Log.i("DriveTo", "Action Complete at " + cadbot.deadWheelTracker.toString() + " distance=" + distance + "  dyTot=" + cadbot.deadWheelTracker.dyTot);
            return;
        }

        controllerInput[0] = speed;
        controllerInput[1] = distance;

        Vector2D powBot = GameField.fieldToRobot(toTarget.unitVector(), position.getHeading()); // the acceleration in bot coords

        // DISPATCH POWER SETTINGS TO THE DRIVETRAIN
        //double power = powerController.controlValue(controllerInput);
        //powBot.multiplyBy(power);
        powBot.multiplyBy(0.0); // kill the movement
        cadbot.mecanumController.setDrivePowerRelativeToRobot(powBot.getX(), -powBot.getY(), yaw);

        Log.e("DriveTo", cadbot.deadWheelTracker.toString() + "   powBot=" + powBot.toString() + yaw + "   d=" + distance + "  e=" + error + " toT=" + toTarget);
    }
}

