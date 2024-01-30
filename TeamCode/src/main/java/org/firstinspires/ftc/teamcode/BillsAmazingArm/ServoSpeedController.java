package org.firstinspires.ftc.teamcode.BillsAmazingArm;

import org.firstinspires.ftc.vision.apriltag.AprilTagProcessorImpl;

public class ServoSpeedController {
    private double targetPos; // where it will eventually go
    private double pos; // where it currently is, by setting the servo motor to this
    private double speed; // how fast it is turning
    private double maxAcc; // the maximum acceleration
    private double threshold; // the amount of difference in error that is allowed to be done
    private double acc; // the acceleration

    public void setTargetPos(double targetPos){
        this.targetPos = targetPos;
    }

    public void update(double avgDt){
        double toTarget = targetPos - pos;
        double distance = Math.abs(toTarget);
        double dStop = -.5 * maxAcc * avgDt * avgDt * Math.signum(speed) + speed * avgDt;
        // if at the target, just stop
        if(distance < threshold){
            acc = 0.0;
            speed = 0.0; // a lazy way of stopping
            pos = targetPos;
            return;
        }
        // else if within stopping distance, slow down
        else if(distance < dStop)
        {
            acc = -maxAcc;
        }
        else{
            acc = maxAcc;
        }
        // else accelerate toward the target
        pos += speed * avgDt + .5 * acc * avgDt * avgDt;
        speed += acc * avgDt; // accelerate toward the target if we need to go

        // now just set the servo to this position
    }
}
