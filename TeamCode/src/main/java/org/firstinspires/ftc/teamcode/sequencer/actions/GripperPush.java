package org.firstinspires.ftc.teamcode.sequencer.actions;

import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;

public class GripperPush implements RobotAction{

    private boolean done = false;
    private double seconds;
    private boolean push = false; // false is pull
    private Cadbot cadbot;
    private double interval; // total time interval of push

    public GripperPush(Cadbot cadbot, double seconds, boolean push){
        this.cadbot = cadbot;
        this.seconds = seconds;
        this.push = push;
    }

    @Override
    public void update() {
        // run gripper forward or backward for an amount of time based on the value i
        double dt = cadbot.deadWheelTracker.getTime();
        interval += dt;
        if(interval > seconds){
            cadbot.motorPool.setGripperStop();
            done = true;
        }
        else{
            if(push)
                cadbot.motorPool.setGripperPush();
            else
                cadbot.motorPool.setGripperPull();
        }
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
