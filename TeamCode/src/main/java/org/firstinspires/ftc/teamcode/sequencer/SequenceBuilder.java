package org.firstinspires.ftc.teamcode.sequencer;

import android.util.Log;

import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmPoseXZ;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D1;
import org.firstinspires.ftc.teamcode.sequencer.actions.DriveTo;
import org.firstinspires.ftc.teamcode.sequencer.actions.GripperPush;
import org.firstinspires.ftc.teamcode.sequencer.actions.MoveArmTo;
import org.firstinspires.ftc.teamcode.sequencer.actions.PlaceBackwardOnSpike;
import org.firstinspires.ftc.teamcode.sequencer.actions.PlaceForwardOnSpike;
import org.firstinspires.ftc.teamcode.sequencer.actions.RetractArm;
import org.firstinspires.ftc.teamcode.sequencer.actions.RobotAction;
import org.firstinspires.ftc.teamcode.sequencer.actions.RotateTo;
import org.firstinspires.ftc.teamcode.sequencer.actions.ScanBackwardForTagLocation;
import org.firstinspires.ftc.teamcode.sequencer.actions.ScanForSmartStouts;
import org.firstinspires.ftc.teamcode.sequencer.actions.ScanForwardForTagLocation;
import org.firstinspires.ftc.teamcode.sequencer.actions.Wait;

/**
 * This class allows an elegant way to construct an action sequence.
 */
public class SequenceBuilder {

    private ActionSequence sequence = new ActionSequence();
    private Cadbot cadbot;

    public SequenceBuilder(Cadbot cadbot){
        if(cadbot == null)
            Log.e("SequenceBuilder", "Cadbot is null in constructor");
        this.cadbot = cadbot;
    }

    public ActionSequence build(){
        return sequence;
    }

    public SequenceBuilder add(RobotAction action){
        if(sequence == null){
            sequence = new ActionSequence();
        }
        sequence.add(action);
        return this;
    }

    public SequenceBuilder driveTo(Vector2D position, double heading){
        if(cadbot == null)
            Log.e("SequenceBuilder", "null cadbot");
        if(position == null)
            Log.e("SequenceBuilder", "null position");
        try {
            sequence.add(new DriveTo(cadbot, new Vector2D1(position, heading)));
        }
        catch (Exception e){
            Log.e("SequenceBuilder", e.toString());
        }
        return this;
    }

    public SequenceBuilder driveTo(double x, double y, double heading){
        sequence.add(new DriveTo(cadbot, new Vector2D1(x, y, heading)));
        return this;
    }

    public SequenceBuilder rotateTo(Vector2D position, double heading){
        sequence.add(new RotateTo(cadbot, new Vector2D1(position, heading)));
        return this;
    }

    public SequenceBuilder rotateTo(double x, double y, double heading){
        sequence.add(new RotateTo(cadbot, new Vector2D1(x, y, heading)));
        return this;
    }

    public SequenceBuilder scanForSmartStout(){
        sequence.add(new ScanForSmartStouts(cadbot));
        return this;
    }

    public SequenceBuilder placeForwardOnSpike(){
        sequence.add(new PlaceForwardOnSpike());
        return this;
    }

    public SequenceBuilder moveArmTo(ArmPoseXZ targetArmPoseXZ){
        sequence.add(new MoveArmTo(cadbot, targetArmPoseXZ));
        return this;
    }


    public SequenceBuilder placeBackwardOnSpike(){
        sequence.add(new PlaceBackwardOnSpike());
        return this;
    }

    public SequenceBuilder retractArm(){
        sequence.add(new RetractArm());
        return this;
    }

    public SequenceBuilder append(ActionSequence s){
        sequence.append(s);
        return this;
    }

    public SequenceBuilder scanBackwardForLocation(){
        sequence.add(new ScanBackwardForTagLocation(cadbot));
        return this;
    }

    public SequenceBuilder scanForwardForLocation(){
        sequence.add(new ScanForwardForTagLocation(cadbot));
        return this;
    }

    public SequenceBuilder gripperPush(double seconds, boolean push){
        sequence.add(new GripperPush(cadbot, seconds, push));
        return this;
    }

    public SequenceBuilder wait(double duration){
        sequence.add(new Wait(cadbot, duration));
        return this;
    }

}
