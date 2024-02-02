package org.firstinspires.ftc.teamcode.sequencer.actions;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;

public class Wait implements RobotAction{
    private boolean done = false;
    private double duration;
    private boolean push = false; // false is pull
    private Cadbot cadbot;
    private ElapsedTime runtime = new ElapsedTime();
    private boolean firstTime = true;

    public Wait(Cadbot cadbot, double duration){
        this.cadbot = cadbot;
        this.duration = duration;
    }

    @Override
    public void update() {
        if(firstTime){
            runtime.reset();
            firstTime = false;
            // start something
        }
        else{
            if(runtime.seconds() > duration){
                // end something
                done = true;
            }
        }
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
