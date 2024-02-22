package org.firstinspires.ftc.teamcode.fuzzy;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UtilityKit;

public class PidController {

    private ElapsedTime runtime = new ElapsedTime();

    private double error;
    private double lastError; // previous error
    private double integralOfError;
    private double derivativeOfError;
    private double kp=1; // proportional gain
    private double ki=0; // integral gain
    private double kd=0; // derivative gain
    private double dt; // delta time

    private double minInput; // the min set point and process value (e.g. speed, acceleration, temperature)
    private double maxInput; // the max set point and process value
    private double minOutput; // range of the control value (e.g. power, btu's, pedal depression)
    private double maxOutput;

    private double acceptableError; // a threshold of tolerance for the error (smaller than this, we don't care about)

    public PidController(double minInput, double maxInput, double minOutput, double maxOutput){
        this.minInput = minInput;
        this.maxInput = maxInput;
        this.minOutput = minOutput;
        this.maxOutput = maxOutput;
    }

    public void setGain(double kp, double ki, double kd){
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
    }

    /**
     * This is the PID controller in action.
     * Set point it the desired value (like speed), process value is the measured result (speed)
     * The output is the controlled value, like the amount of power supplied.
     * @param setPoint
     * @param processValue
     * @return
     */
    public double generate_output(double setPoint, double processValue){
        dt = runtime.seconds(); // get the elapsed time
        runtime.reset();
//        dt = UtilityKit.limitToRange(dt, .000001, .001);
        error = setPoint - processValue;
        integralOfError += (error + lastError) * dt / 2.0;
        derivativeOfError = (error - lastError)/dt;
        lastError = error;
        return kp * error + ki * integralOfError + kd * derivativeOfError;
    }

    public void autotune(double setPointMin, double setPointMax, double outputMin, double outputMax){
        // start with kp = ki = kd = 0
        setGain(0, 0, 0);

        // increase kp gradually until we have oscillations (getting there fast but over-shooting)
        //for(double k=0; k<kMax; k+=.01)
        // reduce kp by half

        // increase ki gradually until we have a fast enough response time

        // increase kd gradually until have damped the oscillations
    }

    // applying power p at vi yields vf and thus e = (vf - vi) or a = e/dt.
    // Recording p -> a tells us the p to use for the desired a.  Store average dt.
}
