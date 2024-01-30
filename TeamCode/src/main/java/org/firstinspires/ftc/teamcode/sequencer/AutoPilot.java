package org.firstinspires.ftc.teamcode.sequencer;

import org.firstinspires.ftc.teamcode.BillsEs.AllianceColor;
import org.firstinspires.ftc.teamcode.BillsEs.AlliancePosition;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D1;
import org.firstinspires.ftc.teamcode.sequencer.actions.DriveTo;
import org.firstinspires.ftc.teamcode.sequencer.actions.RotateTo;

/**
 * The basic autonomous path...
 * forward 1 square
 * if(element at 1 or 3)
 *      rotate 90 to backdrop
 *      place pixel (forward if 1, backward if 3)
 *      strafe 1 tile toward the center
 *      backup 1 tile toward pixel stacks
 *      get one pixel
 * else
 *      forward 1 square
 *      place pixel to back
 *      rotate 90 to backdrop
 *      backup 1 tile toward pixel stacks
 *      get one pixel
 * forward 4 tiles
 * strafe 1 tile toward backdrop
 * approach backdrop
 * identify position
 * strafe to position
 * place 1 pixel
 * strafe to position
 * place 1 pixel
 * backup to center of tile
 * strafe toward center
 * backup 4 tiles
 * get two pixels
 * (repeat the above steps twice, but park after placing the last of the pixels)
 */
public class AutoPilot {

    private Cadbot cadbot;
    public SequenceDirector sequenceDirector;

    private double preferredHeading; // todo: we don't want this here
    public SequenceLibrary sequenceLibrary;

    public void initialize(Cadbot cadbot){
        this.cadbot = cadbot;
        sequenceLibrary = new SequenceLibrary(cadbot);
        sequenceDirector = new SequenceDirector();

        // BUILD THE SEQUENCE
        // if blue
        if(cadbot.allianceColor == AllianceColor.BLUE) {
            preferredHeading = Math.toRadians(-90);
            // if left
            if(cadbot.alliancePosition == AlliancePosition.LEFT) {
                sequenceDirector.addSequence(sequenceLibrary.blueLeft());
            }
            // if right
            else{
                sequenceDirector.addSequence(sequenceLibrary.blueRight());
            }
        }
        // if red
        else{
            preferredHeading = Math.toRadians(90);
            // if left
            if(cadbot.alliancePosition == AlliancePosition.LEFT) {
                sequenceDirector.addSequence(sequenceLibrary.redLeft());
            }
            // if right
            else{
                sequenceDirector.addSequence(sequenceLibrary.redRight());
            }

        }
    }

    public void update(){
        sequenceDirector.update();
    }


}