package org.firstinspires.ftc.teamcode.sequencer;

import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmPoseXZ;
import org.firstinspires.ftc.teamcode.BillsEs.AllianceColor;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D1;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector3D;
import org.firstinspires.ftc.teamcode.sequencer.sequences.armCalibration.ArmTests;
import org.firstinspires.ftc.teamcode.sequencer.sequences.autonomous.BlueLeft;
import org.firstinspires.ftc.teamcode.sequencer.sequences.autonomous.BlueRight;
import org.firstinspires.ftc.teamcode.sequencer.sequences.autonomous.RedLeft;
import org.firstinspires.ftc.teamcode.sequencer.sequences.autonomous.RedRight;

public class SequenceLibrary {

    private Cadbot cadbot;

    public SequenceLibrary(Cadbot cadbot){
        this.cadbot = cadbot;
    }

    private Vector2D centerOfTile(int x, int y){
        return GameField.centerOfTile(x, y);
    }

    public ActionSequence blueLeft(){
        return BlueLeft.start(cadbot);
    }

    public ActionSequence blueRight(){
        return BlueRight.start(cadbot);
//        return new SequenceBuilder(cadbot)
//                .driveTo(centerOfTile(2, 5), Math.toRadians(-90))
//                .rotateTo(centerOfTile(2,  5),Math.toRadians(0))
//                .driveTo(centerOfTile(5, 5),Math.toRadians(0))
//                .driveTo(centerOfTile(5, 4),Math.toRadians(0))
//                .driveTo(centerOfTile(6, 4),Math.toRadians(0))
//                .build();
    }

    public ActionSequence redLeft(){
        return ArmTests.armTest1(cadbot);
        //return RedLeft.start(cadbot);
    }

    public ActionSequence redRight(){
        return RedRight.start(cadbot);
//        return new SequenceBuilder(cadbot)
//                .rotateTo(GameField.centerOfTile(4,1), Math.toRadians(0.0)) // rotate to backdrop
//                .driveTo(GameField.redBackdrop(), Math.toRadians(0.0))  // drive to backdrop
//                .scanForwardForLocation() // testing scan for location... works but not to be used
//                .moveArmTo(ArmPoseXZ.ready())
//                .build();
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
                // todo: scan april tag ?
                .driveTo(centerOfTile(5, 3), 0)
                .build();
    }

    // This action sequence assumes you are parked right in front of the backdrop center
    public ActionSequence placeOnBackdropAt(int row, int col){

        // get the placement coordinates (x is forward, y is left, z is up)
        Vector3D target = GameField.getBackdropPixelPosition(row, col);

        // get backdrop location
        Vector2D backdrop; // a vector to the backdrop location
        if(cadbot.allianceColor == AllianceColor.BLUE){
            backdrop = GameField.blueBackdrop();
        }
        else{ // RED
            backdrop = GameField.redBackdrop();
        }

        // build the sequence, assumes we already facing and near to the center of the backdrop
        return new SequenceBuilder(cadbot)
                .driveTo(backdrop, 0) // move to the backdrop, facing it
                .scanForwardForLocation()
        // strafe to position
                .driveTo(backdrop.add(0.0, target.y), 0)
        // extend arm to target grid location, if sensor available use it to detect touching the backdrop
                .moveArmTo(new ArmPoseXZ(target.x, target.z, Math.toRadians(30.0), 0))
        // gripper pushes a pixel off
                .gripperPush(0.5, true)
        // retract arm
                .moveArmTo(ArmPoseXZ.ready())
        // backup slightly
                .build();
    }
}
