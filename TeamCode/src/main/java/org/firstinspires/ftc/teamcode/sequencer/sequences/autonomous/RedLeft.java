package org.firstinspires.ftc.teamcode.sequencer.sequences.autonomous;

import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.sequencer.engine.ActionSequence;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.GameField;
import org.firstinspires.ftc.teamcode.sequencer.engine.SequenceBuilder;

public class RedLeft {
    public static ActionSequence start(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.betweenTiles(2,2, 2, 1), Math.toRadians(90))
                .scanForSmartStout()
                .build();
    }

    public static ActionSequence leftSpike(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.leftSpike(cadbot).add(0, -5.0), Math.toRadians(90)) // drive to spike
                .driveTo(-48.0, -48.0, Math.toRadians(90)) // backup
                .driveTo(GameField.centerOfTile(2,1), Math.toRadians(90)) // strafe toward wall
                .driveTo(GameField.centerOfTile(2,3).add(3,0), Math.toRadians(90)) // forward to the center
                .rotateTo(GameField.centerOfTile(2,3).add(3, 0), Math.toRadians(0)) // rotate to face the backdrop
                .driveTo(GameField.centerOfTile(6, 3).add(3, 0), Math.toRadians(0)) // forward down the center
                .gripperPush(2.0, true)
                .driveTo(GameField.centerOfTile(6, 3).add(-3, 0), Math.toRadians(0)) // forward down the center
//                .driveTo(GameField.redBackdrop(), Math.toRadians(0)) // to the backdrop
                .build();
    }

    public static ActionSequence rightSpike(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.centerOfTile(2, 2), Math.toRadians(90))  // forward to tile
                .rotateTo(GameField.centerOfTile(2, 2), Math.toRadians(0))  // rotate to backdrop
                .driveTo(GameField.rightSpike(cadbot).add(-5.0, -5.0), Math.toRadians(0)) // drive to spike
                .driveTo(GameField.centerOfTile(2,2), Math.toRadians(0)) // backup
                .driveTo(GameField.centerOfTile(2, 3), Math.toRadians(0)) // strafe to center isle
                .driveTo(GameField.centerOfTile(6, 3), Math.toRadians(0)) // forward down the center
                .gripperPush(2.0, true)
                .driveTo(GameField.centerOfTile(6, 3).add(-3, 0), Math.toRadians(0)) // forward down the center
//                .driveTo(GameField.redBackdrop(), Math.toRadians(0)) // to the backdrop
                .build();
    }

    public static ActionSequence middleSpike(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.centerSpike(cadbot).add(0, -5.0), Math.toRadians(90)) // drive to spike
                .driveTo(GameField.betweenTiles(2,2, 2, 1), Math.toRadians(90)) // backup
                .driveTo(GameField.betweenTiles(1,2, 1, 1).add(3, 0), Math.toRadians(90)) // strafe toward wall
                .driveTo(GameField.centerOfTile(1,3).add(3, 0), Math.toRadians(90)) // forward to the center
                .rotateTo(GameField.centerOfTile(1,3).add(3,0), Math.toRadians(0)) // rotate to face the backdrop
                .driveTo(GameField.centerOfTile(6, 3), Math.toRadians(0)) // forward down the center
                .gripperPush(2.0, true)
                .driveTo(GameField.centerOfTile(6, 3).add(-3, 0), Math.toRadians(0)) // forward down the center
//                .driveTo(GameField.redBackdrop(), Math.toRadians(0)) // to the backdrop
                .build();
    }


}
