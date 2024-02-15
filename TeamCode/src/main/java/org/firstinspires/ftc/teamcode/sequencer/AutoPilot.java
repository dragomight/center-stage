package org.firstinspires.ftc.teamcode.sequencer;

import org.firstinspires.ftc.teamcode.BillsEs.AllianceColor;
import org.firstinspires.ftc.teamcode.BillsEs.AlliancePosition;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.sequencer.sequences.arm.ArmTests;
import org.firstinspires.ftc.teamcode.sequencer.sequences.arm.Backdrop;
import org.firstinspires.ftc.teamcode.sequencer.sequences.autonomous.BlueLeft;
import org.firstinspires.ftc.teamcode.sequencer.sequences.autonomous.BlueRight;
import org.firstinspires.ftc.teamcode.sequencer.sequences.autonomous.RedLeft;
import org.firstinspires.ftc.teamcode.sequencer.sequences.autonomous.RedRight;

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

    public void initialize(Cadbot cadbot){
        this.cadbot = cadbot;
        sequenceDirector = new SequenceDirector();

        if(!cadbot.isRunningAutonomous())
            return;

        // SET THE SEQUENCE
        // if blue
        if(cadbot.allianceColor == AllianceColor.BLUE) {
            // if left
            if(cadbot.alliancePosition == AlliancePosition.LEFT) {
                sequenceDirector.addSequence(BlueLeft.start(cadbot));
            }
            // if right
            else{
                sequenceDirector.addSequence(BlueRight.start(cadbot));
            }
        }
        // if red
        else{
            // if left
            if(cadbot.alliancePosition == AlliancePosition.LEFT) {
                sequenceDirector.addSequence(RedLeft.start(cadbot));
            }
            // if right
            else{
                sequenceDirector.addSequence(RedRight.start(cadbot));
            }
        }
    }

    public void update(){
        sequenceDirector.update();
    }

    public void runArmTest(){
        sequenceDirector.addSequence(ArmTests.armTest1(cadbot));
    }

    public void placeOnBackdrop(int row, int col){
        sequenceDirector.addSequence(Backdrop.placePixel(cadbot, row, col));
    }


}
