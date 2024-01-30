package org.firstinspires.ftc.teamcode.sequencer;

import org.firstinspires.ftc.teamcode.BillsEs.AllianceColor;
import org.firstinspires.ftc.teamcode.BillsTensorTunes.SpikeMark;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D1;

public class SequenceLibrary {

    private Cadbot cadbot;

    public SequenceLibrary(Cadbot cadbot){
        this.cadbot = cadbot;
    }

    private Vector2D centerOfTile(int x, int y){
        return cadbot.gameField.centerOfTile(x, y);
    }

    public ActionSequence blueLeft(){
        return new SequenceBuilder(cadbot)
                .driveTo(centerOfTile(2, 5), Math.toRadians(-90))
                .build();
    }

    public ActionSequence blueRight(){
//        actionSequence.add(new DriveTo(cadbot, new Vector2D1(centerOfTile(2, 5), Math.toRadians(-90))));
//        actionSequence.add(new RotateTo(cadbot, new Vector2D1(centerOfTile(2, 5), Math.toRadians(0))));
//        actionSequence.add(new DriveTo(cadbot, new Vector2D1(centerOfTile(5, 5), Math.toRadians(0))));
//        actionSequence.add(new DriveTo(cadbot, new Vector2D1(centerOfTile(5, 4), Math.toRadians(0))));
//        actionSequence.add(new DriveTo(cadbot, new Vector2D1(centerOfTile(6, 4), Math.toRadians(0))));
        return new SequenceBuilder(cadbot).build();
    }

    public ActionSequence redLeft(){
        return new SequenceBuilder(cadbot)
                .driveTo(centerOfTile(2,2), Math.toRadians(90))
                .scanForSmartStout(SpikeMark.MIDDLE)
                .build();
    }

    public ActionSequence redRight(){
        return new SequenceBuilder(cadbot).build();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    public ActionSequence rotationTest(){
        // we reset the initial position for testing purposes
        cadbot.deadWheelTracker.resetPose(new Vector2D1(0, 0, Math.toRadians(90)));
        return new SequenceBuilder(cadbot)
                .rotateTo(0, 0, Math.toRadians(180))
                .rotateTo(0, 0, Math.toRadians(-90))
                .rotateTo(0, 0, Math.toRadians(0))
                .rotateTo(0, 0, Math.toRadians(90))
                .rotateTo(0, 0, Math.toRadians(0))
                .rotateTo(0, 0, Math.toRadians(-90))
                .rotateTo(0, 0, Math.toRadians(180))
                .rotateTo(0, 0, Math.toRadians(90))
                .build();
    }

    public ActionSequence redScansOtherSpikes(){
        return new SequenceBuilder(cadbot)
                .rotateTo(centerOfTile(2, 2), 0)
                .scanForSmartStout(SpikeMark.RIGHT)
                .build();
    }

    public ActionSequence redPlacesAtMiddleSpike(){
        return new SequenceBuilder(cadbot)
                .placeForwardOnSpike()
                .retractArm()
                .rotateTo(centerOfTile(2, 2), 0)
                .driveTo(centerOfTile(1,2), 0)
                .driveTo(centerOfTile(1,3), 0)
                .append(redLeftUsesCenter())
                .build();
    }

    public ActionSequence redPlacesAtLeftSpike(){
        return new SequenceBuilder(cadbot)
                .placeBackwardOnSpike()
                .retractArm()
                .driveTo(centerOfTile(2, 3), 0)
                .driveTo(centerOfTile(1,3), 0)
                .append(redLeftUsesCenter())
                .build();
    }

    public ActionSequence redPlacesAtRightSpike(){
        return new SequenceBuilder(cadbot)
                .placeForwardOnSpike()
                .retractArm()
                .driveTo(centerOfTile(2, 3), 0)
                .driveTo(centerOfTile(1,3), 0)
                .append(redLeftUsesCenter())
                .build();
    }

    public ActionSequence redLeftUsesCenter(){
        return new SequenceBuilder(cadbot)
                .driveTo(centerOfTile(5, 3), 0)
                .build();
    }

    public ActionSequence placeOnBackdropAt(int x, int y){

        Vector2D backdrop;
        if(cadbot.allianceColor == AllianceColor.BLUE){
            backdrop = cadbot.gameField.BLUE_BACKDROP;
        }
        else{ // RED
            backdrop = cadbot.gameField.RED_BACKDROP;
        }
        return new SequenceBuilder(cadbot)
                .driveTo(backdrop, 0) // move to the backdrop, facing it

        // strafe to position
                .driveTo(backdrop.add(0, y * cadbot.gameField.GRID_Y), 0)
        // approach close, slowly
        // extend arm to target grid location, if sensor available use it to detect the backdrop
        // gripper pushes a pixel off
        // retract arm
        // backup slightly
                .build();
    }
}
