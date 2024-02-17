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
public class DriveTo implements RobotAction{

    // From testing: topSpeed=64.6 but typical topSpeed=50   topAccel=66   topDecel=100
    public static final double DISTANCE_THRESHOLD = 1.0;
    public static final double ANGULAR_THRESHOLD = Math.toRadians(2.0); // the top number is the allowed error in degrees
    public static final double MAXIMUM_ACHIEVABLE_ACCELERATION = 100.0;
    public static final double KP = 2.0;

    private final static double MAX_SPEED = 65; // default maximums if none is given
    private final static double MAX_ACCEL = 40;
    private final static double MAX_DECEL = 40;

    FuzzyController2D powerController = FuzzyPowerController.getPowerController();

    private boolean done = false;
    Cadbot cadbot;
    Vector2D1 target; // the position we tell the robot to drive to
    double maxSpeed = MAX_SPEED; // the fastest we want the robot to drive
    double maxAccel = MAX_ACCEL; // the fastest we want the robot to accelerate
    double maxDecel = MAX_DECEL; // the fastest we want the robot to brake, a positive number
    private double error;
    private double lastDistance = 999999;

    private double[] controllerInput = new double[2];
    double power; // the amount of power being applied to the motors, ranging [-1, 1]

    public DriveTo(Cadbot cadbot, Vector2D1 target, double maxSpeed, double maxAccel, double maxDecel){
        this.cadbot = cadbot;
        this.target = target;
        this.maxSpeed = maxSpeed;
        this.maxAccel = maxAccel;
        this.maxDecel = maxDecel;
        Log.e("DriveTo", "Creating DriveTo to target: " + target.toString());
    }

    public DriveTo(Cadbot cadbot, Vector2D1 target){
        this.cadbot = cadbot;
        this.target = target;
        Log.e("DriveTo", "Creating DriveTo to target: " + target.toString());
    }

    public DriveTo(Cadbot cadbot, Vector2D position, double heading){
        this.cadbot = cadbot;
        this.target = new Vector2D1(position, heading);
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

        // velocity and speed
        Vector2D v = new Vector2D(velocity.getX(), velocity.getY());
        double speed = v.magnitude();

        // DETERMINE POWER LEVEL TO MAINTAIN THE PREFERRED HEADING:
        // set the value of the yaw using a PID controller
        error = -Vector2D1.angularDifference(target.getHeading(), position.getHeading())/Math.PI;
        double yaw = error * KP; // error is already small
        yaw = UtilityKit.limitToRange(yaw, -1.0, 1.0);

        // CHECK IF THE ACTION IS DONE
        // if we are already within threshold tolerance of the target pose (x, y, bearing), set done = true
        if((distance < DISTANCE_THRESHOLD && speed == 0.0 && Math.abs(error) < ANGULAR_THRESHOLD && velocity.getHeading() == 0)){ //|| distance > lastDistance) ){ // && Math.abs(error) < ANGULAR_THRESHOLD){
            cadbot.mecanumController.setDrivePowerRelativeToRobot(0, 0, 0);
            done = true;
            Log.i("DriveTo", "Action Complete at " + cadbot.deadWheelTracker.toString() + " distance=" + distance);
            return;
        }

//        if(distance < lastDistance){
//            lastDistance = distance;
//        }

        controllerInput[0] = speed;
        controllerInput[1] = distance;

        // CALCULATE THE TARGET ACCELERATION
//        double avgDt = cadbot.deadWheelTracker.getAvgDt();
//        if(avgDt == 0)
//            return;
//        double dMag = Math.min(distance, avgDt * TOP_SPEED);
//        Vector2D d = toTarget.unitVector().multiplyBy(dMag); // this is the desired displacement
//        double avgDt2 = avgDt*avgDt;
//        Vector2D a = new Vector2D((2 * (d.getX() - velocity.getX() * avgDt)/avgDt2),
//                                  (2 * (d.getY() - velocity.getY() * avgDt)/avgDt2)); // the target acceleration

        Vector2D powBot = GameField.fieldToRobot(toTarget.unitVector(), position.getHeading()); // the acceleration in bot coords
//        Vector2D power = new Vector2D(aBot.getX()/MAXIMUM_ACHIEVABLE_ACCELERATION, aBot.getY()/MAXIMUM_ACHIEVABLE_ACCELERATION);
        // DISPATCH POWER SETTINGS TO THE DRIVETRAIN
        double power = powerController.controlValue(controllerInput);
        powBot.multiplyBy(power);
        cadbot.mecanumController.setDrivePowerRelativeToRobot(powBot.getX(), -powBot.getY(), yaw);

//        Log.e("DriveTo", cadbot.deadWheelTracker.toString() + "   powBot=" + powBot.toString() + yaw + "   d=" + distance + "  e=" + Math.toDegrees(error) + " toT=" + toTarget);
//        Log.e("DriveTo", "target=" + target.toString());
//        Log.e("DriveTo", "toTarget="+toTarget.toString() + "   dist=" + distance + "   dMag=" + dMag);
//        Log.e("DriveTo", "d=" + d.toString());
//        Log.e("DriveTo", "a=" + a.toString());
//        Log.e("DriveTo", "   powBot=" + powBot.toString());
//        Log.e("DriveTo", "power=" + power.toString() + ",... " + yaw);

        // DISPATCH POWER SETTINGS TO THE DRIVETRAIN
//        cadbot.mecanumController.setDrivePowerRelativeToRobot(toTargetRobotCoords.getX(), -toTargetRobotCoords.getY(), yaw);
//
//        // DETERMINE POWER LEVEL TO MOVE TO THE TARGET POSITION (x, y)
//        // given a position and velocity and target position, we will be in one of three cases:
//        // 1) wanting to accelerate because the target is outside stopping distance and we are below top speed
//        // 2) wanting to maintain speed because we are near the top speed even when outside of stopping distance
//        // 3) decelerating because the target is within stopping distance
//        // this function determines the target acceleration and angular acceleration
//        Vector2D v = new Vector2D(velocity.getX(), velocity.getY());
//        double speed = v.magnitude();
//        // calculate the braking distance
//        double dStop = speed * speed / (2 * maxDecel); // decel being positive
//
//        // determine acceleration
//        double acc = 0;
//        // if outside the acceleration-deceleration zone,
//        if(distance > dStop){
//            // accelerate or decelerate to approach maxSpeed, but no more than maxAccel
//            acc = (maxSpeed - speed)/cadbot.deadWheelTracker.getAvgDt();
//            acc = UtilityKit.limitToRange(acc, -maxDecel, maxAccel);
//        }
//        // if inside the acceleration-deceleration zone,
//        else{
//            acc = -maxDecel;
//        }
//
//        // modify the power to match the desired acceleration
//        power += acc/MAXIMUM_ACHIEVABLE_ACCELERATION;
//        power = UtilityKit.limitToRange(power, -1.0, 1.0);
//
//        Log.e("DriveTo", cadbot.deadWheelTracker.toString());
//        Log.e("DriveTo", "target=" + target.toString());
//        Log.e("DriveTo", "toTarget="+toTarget.toString());
//        Log.e("DriveTo", "distance=" + distance + "    dStop=" + dStop + "    speed=" + speed + "    acc="+acc + "    pow=" + power + "   err=" + error);
//
//        // todo: power is not used!
//        // map toTarget to [-1, 1]
//        Vector2D unitToTarget = toTarget.unitVector(); //.multiplyBy(power);
//
//        // transform that vector to robot coordinates
//        Vector2D toTargetRobotCoords = cadbot.gameField.fieldToRobot(unitToTarget, position.getHeading());
//
////        cadbot.telemetry.addData("DriveTo: x", pose2d.getX());
////        cadbot.telemetry.addData("DriveTo: y", pose2d.getY());
////        cadbot.telemetry.addData("DriveTo: h", yaw);
//
//        // DISPATCH POWER SETTINGS TO THE DRIVETRAIN
//        cadbot.mecanumController.setDrivePowerRelativeToRobot(toTargetRobotCoords.getX(), -toTargetRobotCoords.getY(), yaw);
//        Log.e("DriveTo", "DrivePow(robot-coords)="+toTargetRobotCoords.toString() + "  yaw=" + yaw);
//        // todo: should the y coord be negative???, it doesn;t match the printout
    }
}
