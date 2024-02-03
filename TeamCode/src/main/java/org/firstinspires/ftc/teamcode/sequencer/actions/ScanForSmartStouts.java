package org.firstinspires.ftc.teamcode.sequencer.actions;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.BillsEs.AllianceColor;
import org.firstinspires.ftc.teamcode.BillsEs.AlliancePosition;
import org.firstinspires.ftc.teamcode.BillsTensorTunes.SpikeMark;
import org.firstinspires.ftc.teamcode.BillsTensorTunes.ThirdEyeSurfer;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.sequencer.sequences.autonomous.BlueLeft;
import org.firstinspires.ftc.teamcode.sequencer.sequences.autonomous.BlueRight;
import org.firstinspires.ftc.teamcode.sequencer.sequences.autonomous.RedLeft;
import org.firstinspires.ftc.teamcode.sequencer.sequences.autonomous.RedRight;

public class ScanForSmartStouts implements RobotAction{

    private boolean done = false;
    private Cadbot cadbot;
    private Telemetry telemetry;
    private ThirdEyeSurfer thirdEyeSurfer;
    private int iteration;
    private double maxConfidence;
    private Recognition bestStout;
    public final static double LEFT = 200;
    public final static double RIGHT = 400;

    public ScanForSmartStouts(Cadbot cadbot){
        this.cadbot = cadbot;
        this.telemetry = cadbot.telemetry;
        this.thirdEyeSurfer = cadbot.thirdEyeSurfer;
        Log.e("ScanForSmartStouts", "Created ScanForSmartStout");
    }

    @Override
    public void update() {
        if (cadbot.thirdEyeSurfer == null) {
            Log.e("ScanForSmartStouts", "No Third Eye Surfer available.");
            return;
        } else {
            this.thirdEyeSurfer = cadbot.thirdEyeSurfer;
        }

        // run the scan on the front camera until we sense something or nothing with confidence
        Recognition stout = cadbot.thirdEyeSurfer.frontScan();
        if (stout != null) {
            if (stout.getConfidence() > maxConfidence) {
                maxConfidence = stout.getConfidence();
                bestStout = stout;
            }
        }
        Log.e("ScanForSmartStouts", "MaxConf=" + maxConfidence);

        if (iteration++ > 5000 || maxConfidence > .7) {
            if (bestStout != null) {
                double x = (bestStout.getLeft() + bestStout.getRight()) / 2;
                double y = (bestStout.getTop() + bestStout.getBottom()) / 2;
                if (x < LEFT) {
                    Log.e("ScanForSmartStouts", "Selecting LEFT at x=" + x + "  MaxConf=" + maxConfidence);
                    cadbot.spikeMark = SpikeMark.LEFT;
                    left();
                } else if (x > RIGHT) {
                    Log.e("ScanForSmartStouts", "Selecting RIGHT at x=" + x + "  MaxConf=" + maxConfidence);
                    cadbot.spikeMark = SpikeMark.RIGHT;
                    right();
                } else {
                    Log.e("ScanForSmartStouts", "Selecting CENTER at x=" + x + "  MaxConf=" + maxConfidence);
                    cadbot.spikeMark = SpikeMark.MIDDLE;
                    middle();
                }
            } else {
                // we shall assume its in the middle
                Log.e("ScanForSmartStouts", "NO SELECTION MaxConf=" + maxConfidence);
                cadbot.spikeMark = SpikeMark.MIDDLE;
                middle();
            }

            done = true;
            return;
        }
    }

    private void left(){
        if(cadbot.allianceColor == AllianceColor.RED){
            if(cadbot.alliancePosition == AlliancePosition.LEFT){
                cadbot.autoPilot.sequenceDirector.addSequence(RedLeft.leftSpike(cadbot));
            }
            else{ // RIGHT
                cadbot.autoPilot.sequenceDirector.addSequence(RedRight.leftSpike(cadbot));
            }
        }
        else{ // BLUE
            if(cadbot.alliancePosition == AlliancePosition.LEFT){
                cadbot.autoPilot.sequenceDirector.addSequence(BlueLeft.leftSpike(cadbot));
            }
            else{ // RIGHT
                cadbot.autoPilot.sequenceDirector.addSequence(BlueRight.leftSpike(cadbot));
            }
        }
    }

    private void right(){
        if(cadbot.allianceColor == AllianceColor.RED){
            if(cadbot.alliancePosition == AlliancePosition.LEFT){
                cadbot.autoPilot.sequenceDirector.addSequence(RedLeft.rightSpike(cadbot));
            }
            else{ // RIGHT
                cadbot.autoPilot.sequenceDirector.addSequence(RedRight.rightSpike(cadbot));
            }
        }
        else{ // BLUE
            if(cadbot.alliancePosition == AlliancePosition.LEFT){
                cadbot.autoPilot.sequenceDirector.addSequence(BlueLeft.rightSpike(cadbot));
            }
            else{ // RIGHT
                cadbot.autoPilot.sequenceDirector.addSequence(BlueRight.rightSpike(cadbot));
            }
        }
    }

    private void  middle(){
        if(cadbot.allianceColor == AllianceColor.RED){
            if(cadbot.alliancePosition == AlliancePosition.LEFT){
                cadbot.autoPilot.sequenceDirector.addSequence(RedLeft.middleSpike(cadbot));
            }
            else{ // RIGHT
                cadbot.autoPilot.sequenceDirector.addSequence(RedRight.middleSpike(cadbot));
            }
        }
        else{ // BLUE
            if(cadbot.alliancePosition == AlliancePosition.LEFT){
                cadbot.autoPilot.sequenceDirector.addSequence(BlueLeft.middleSpike(cadbot));
            }
            else{ // RIGHT
                cadbot.autoPilot.sequenceDirector.addSequence(BlueRight.middleSpike(cadbot));
            }
        }
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
