package org.firstinspires.ftc.teamcode.sequencer.actions;

import android.util.Log;

import org.firstinspires.ftc.teamcode.BillsEs.AllianceColor;
import org.firstinspires.ftc.teamcode.BillsEs.AlliancePosition;
import org.firstinspires.ftc.teamcode.BillsTensorTunes.SpikeMark;
import org.firstinspires.ftc.teamcode.BillsTensorTunes.ThirdEyeSurfer;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;

/**
 * Detects the position of the team element, records this information, and adds a new playlist.
 */
public class ScanForSmartStout implements RobotAction{

    private boolean done = false;
    private Cadbot cadbot;
    private SpikeMark readingSpike;
    private ThirdEyeSurfer thirdEyeSurfer;

    public ScanForSmartStout(Cadbot cadbot, SpikeMark spikeMark){
        this.cadbot = cadbot;
        this.readingSpike = spikeMark;
        this.thirdEyeSurfer = cadbot.thirdEyeSurfer;
        Log.e("ScanForSmartStout", "Created SFSS Action for spike " + spikeMark);
    }

    @Override
    public void update() {
        if(cadbot.thirdEyeSurfer == null){
            Log.e("ScanForSmartStout", "No Third Eye Surfer available.");
            return;
        }
        else{
            this.thirdEyeSurfer = cadbot.thirdEyeSurfer;
        }
        // IF DOING THE MIDDLE SPIKE SCAN
        if(readingSpike == SpikeMark.MIDDLE) {
            // run the scan on the front camera until we sense something or nothing with confidence
            double conf = cadbot.thirdEyeSurfer.frontScan();
            Log.e("ScanForSmartStout", "Scanning Middle Spike: conf=" + conf);

            // if see ale... store the information and load the place at the middle spike action sequence
            if(conf > .7) {
                cadbot.spikeMark = SpikeMark.MIDDLE;
                if (cadbot.allianceColor == AllianceColor.BLUE) {
                    //cadbot.autoPilot.sequenceDirector.addSequence(cadbot.autoPilot.sequenceLibrary.bluePlaceAtMiddleSpike());
                }
                // if RED
                else {
//                    cadbot.autoPilot.sequenceDirector.addSequence(cadbot.autoPilot.sequenceLibrary.redPlacesAtMiddleSpike());
                }
                //done = true;
                return;
            }
            // else, no ale on the bar, try the right spike
            else{
                if (cadbot.allianceColor == AllianceColor.BLUE) {
                    //cadbot.autoPilot.sequenceDirector.addSequence(cadbot.autoPilot.sequenceLibrary.blueScanOtherSpikes());
                }
                // if RED
                else {
//                    cadbot.autoPilot.sequenceDirector.addSequence(cadbot.autoPilot.sequenceLibrary.redScansOtherSpikes());
                }
//                done = true;
                return;
            }
        }

        // IF READING THE LEFT AND RIGHT SPIKES
        else if(readingSpike == SpikeMark.LEFT || readingSpike == SpikeMark.RIGHT){
            // read the front and back cameras and use the one with the highest degree of confidence for placement
            double confLeft = thirdEyeSurfer.frontScan();
            double confRight = thirdEyeSurfer.backScan();

            // for red, front is right and back is left
            if(cadbot.allianceColor == AllianceColor.BLUE){
                double swap = confLeft;
                confLeft = confRight;
                confRight = swap;
            }
            // for blue, front is left and back is right
            if(confLeft > confRight){
                cadbot.spikeMark = SpikeMark.LEFT;
            }
            else{
                cadbot.spikeMark = SpikeMark.RIGHT;
            }
            Log.e("ScanForSmartStout", "confLeft=" + confLeft + "   confRight=" + confRight);

            // TRIGGER THE NEXT SEQUENCE
            if(cadbot.allianceColor == AllianceColor.BLUE){
                if(cadbot.spikeMark == SpikeMark.LEFT) {
                    //cadbot.autoPilot.sequenceDirector.addSequence(cadbot.autoPilot.sequenceLibrary.bluePlaceAtLeftSpike());
                }
                else{ // spike RIGHT
                    //cadbot.autoPilot.sequenceDirector.addSequence(cadbot.autoPilot.sequenceLibrary.bluePlaceAtRightSpike());
                }
            }
            // Alliance is RED
            else{
                if(cadbot.spikeMark == SpikeMark.LEFT) {
                    cadbot.autoPilot.sequenceDirector.addSequence(cadbot.autoPilot.sequenceLibrary.redPlacesAtLeftSpike());
                }
                else{ // spike RIGHT
                    cadbot.autoPilot.sequenceDirector.addSequence(cadbot.autoPilot.sequenceLibrary.redPlacesAtRightSpike());
                }
            }
            done = true;
            return;
        }
        // this should never happen
        else{
            done = true;
            return;
        }
    }

    @Override
    public boolean isDone() {
        return done;
    }
}
