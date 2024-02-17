package org.firstinspires.ftc.teamcode.sequencer.actions;

import android.util.Log;

import org.firstinspires.ftc.teamcode.BillsTensorTunes.ThirdEyeSurfer;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D1;

public class ScanForwardForTagLocation implements RobotAction{

    private Cadbot cadbot;
    private boolean done = false;

    public ScanForwardForTagLocation(Cadbot cadbot){
        this.cadbot = cadbot;
    }

    int i=0;
    @Override
    public void update() {
        if(cadbot.thirdEyeSurfer == null){
            Log.e("ScanForwardForTagLocation", "Scan failed!  ThirdEyeSurfer is null");
        }
        Vector2D1 pose = cadbot.thirdEyeSurfer.scanForwardForTagLocation();
        if(pose != null) {
            Log.e("ScanForwardForTagLocation" , "wheelPose=" + cadbot.deadWheelTracker.getPose() + "  eyePose=" + pose);
            cadbot.deadWheelTracker.resetPose(pose);
            done = true;
        }
        else
            Log.e("ScanForwardForTagLocation", "Null pose");
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
