package org.firstinspires.ftc.teamcode.sequencer.actions;

import org.firstinspires.ftc.teamcode.BillsTensorTunes.ThirdEyeSurfer;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D1;

public class ScanBackwardForTagLocation implements RobotAction{

    private Cadbot cadbot;
    private ThirdEyeSurfer thirdEyeSurfer;
    private boolean done = false;

    public ScanBackwardForTagLocation(Cadbot cadbot){
        this.cadbot = cadbot;
        thirdEyeSurfer = cadbot.thirdEyeSurfer;
    }

    @Override
    public void update() {
        Vector2D1 pose = thirdEyeSurfer.scanBackwardForTagLocation();
        if(pose != null)
            cadbot.deadWheelTracker.resetPose(pose);
        done = true;
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
