package org.firstinspires.ftc.teamcode.sequence;

import org.firstinspires.ftc.teamcode.BillsEs.AllianceColor;
import org.firstinspires.ftc.teamcode.BillsEs.AlliancePosition;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D1;

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
    private SequenceDirector sequenceDirector = new SequenceDirector();
    private final static double MAX_SPEED = 10;
    private final static double MAX_ACCEL = 40;
    private final static double MAX_DECEL = 40;

    private Vector2D target;
    private Vector2D1 target2D1;
    private double preferredHeading;
    ActionSequence actionSequence = new ActionSequence();


    public void initialize(Cadbot cadbot){
        this.cadbot = cadbot;

        // BUILD THE SEQUENCE
        // if blue
        if(cadbot.allianceColor == AllianceColor.BLUE) {
            preferredHeading = Math.toRadians(-90);
            // if left
            if(cadbot.alliancePosition == AlliancePosition.LEFT) {
                blueLeft();
            }
            // if right
            else{
                blueRight();
            }
        }
        // if red
        else{
            preferredHeading = Math.toRadians(90);
            // if left
            if(cadbot.alliancePosition == AlliancePosition.LEFT) {
                redLeft();
            }
            // if right
            else{
                redRight();
            }

        }

        sequenceDirector.addSequence(actionSequence);
    }

    public void update(){
        sequenceDirector.update();
    }

    private Vector2D centerOfTile(int x, int y){
        return cadbot.gameField.centerOfTile(x, y);
    }

    private void blueLeft(){
        target2D1 = new Vector2D1(centerOfTile(2, 5), Math.toRadians(-90));
        actionSequence.add(new DriveTo(cadbot, target2D1, MAX_SPEED, MAX_ACCEL, MAX_DECEL));
        // ActionSequence as = SequenceBuilder.driveForward(centerOfTile(2, 5), -90)
        //                                    .driveBackward()
    }

    private void blueRight(){
        target2D1 = new Vector2D1(centerOfTile(4, 5), Math.toRadians(-90));
        actionSequence.add(new DriveTo(cadbot, target2D1, MAX_SPEED, MAX_ACCEL, MAX_DECEL));
    }

    private void redLeft(){
        cadbot.deadWheelTracker.resetPose(new Vector2D1(0, 0, Math.toRadians(90)));
        actionSequence.add(new RotateTo(cadbot, new Vector2D1(0, 0, Math.toRadians(180)), MAX_SPEED, MAX_ACCEL, MAX_DECEL));
        actionSequence.add(new RotateTo(cadbot, new Vector2D1(0, 0, Math.toRadians(-90)), MAX_SPEED, MAX_ACCEL, MAX_DECEL));
        actionSequence.add(new RotateTo(cadbot, new Vector2D1(0, 0, Math.toRadians(0)), MAX_SPEED, MAX_ACCEL, MAX_DECEL));
        actionSequence.add(new RotateTo(cadbot, new Vector2D1(0, 0, Math.toRadians(90)), MAX_SPEED, MAX_ACCEL, MAX_DECEL));
        actionSequence.add(new RotateTo(cadbot, new Vector2D1(0, 0, Math.toRadians(0)), MAX_SPEED, MAX_ACCEL, MAX_DECEL));
        actionSequence.add(new RotateTo(cadbot, new Vector2D1(0, 0, Math.toRadians(-90)), MAX_SPEED, MAX_ACCEL, MAX_DECEL));
        actionSequence.add(new RotateTo(cadbot, new Vector2D1(0, 0, Math.toRadians(180)), MAX_SPEED, MAX_ACCEL, MAX_DECEL));
        actionSequence.add(new RotateTo(cadbot, new Vector2D1(0, 0, Math.toRadians(90)), MAX_SPEED, MAX_ACCEL, MAX_DECEL));
//        Vector2D1 target1 = new Vector2D1(centerOfTile(2, 2), Math.toRadians(90));
//        actionSequence.add(new RotateTo(cadbot, target1, MAX_SPEED, MAX_ACCEL, MAX_DECEL));
//        Vector2D1 target2 = new Vector2D1(centerOfTile(2, 2), Math.toRadians(180));
//        actionSequence.add(new RotateTo(cadbot, target2, MAX_SPEED, MAX_ACCEL, MAX_DECEL));
//        Vector2D1 target3 = new Vector2D1(centerOfTile(2, 2), Math.toRadians(-90));
//        actionSequence.add(new RotateTo(cadbot, target3, MAX_SPEED, MAX_ACCEL, MAX_DECEL));
//        Vector2D1 target4 = new Vector2D1(centerOfTile(2, 2), Math.toRadians(0));
//        actionSequence.add(new RotateTo(cadbot, target4, MAX_SPEED, MAX_ACCEL, MAX_DECEL));
//        Vector2D1 target5 = new Vector2D1(centerOfTile(2, 2), Math.toRadians(90));
//        actionSequence.add(new RotateTo(cadbot, target5, MAX_SPEED, MAX_ACCEL, MAX_DECEL));
////        Vector2D1 target6 = new Vector2D1(centerOfTile(2, 2), Math.toRadians(180));
//        actionSequence.add(new RotateTo(cadbot, target6, MAX_SPEED, MAX_ACCEL, MAX_DECEL));

 //       target2D1 = new Vector2D1(centerOfTile(2, 2), Math.toRadians(0));
 //       actionSequence.add(new DriveTo(cadbot, target2D1, MAX_SPEED, MAX_ACCEL, MAX_DECEL));
    }

    private void redRight(){
        target2D1 = new Vector2D1(centerOfTile(4, 2), Math.toRadians(90));
        actionSequence.add(new DriveTo(cadbot, target2D1, MAX_SPEED, MAX_ACCEL, MAX_DECEL));
    }
}
