package org.firstinspires.ftc.teamcode.sequencer.sequences.autonomous;

import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.sequencer.ActionSequence;
import org.firstinspires.ftc.teamcode.sequencer.GameField;
import org.firstinspires.ftc.teamcode.sequencer.SequenceBuilder;

public class BlueRight {
    public static ActionSequence start(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.betweenTiles(2,6, 2, 5), Math.toRadians(-90))
                .scanForSmartStout()
                .build();
    }

    public static ActionSequence leftSpike(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.centerOfTile(2, 5), Math.toRadians(-90))
                .rotateTo(GameField.centerOfTile(2, 5), Math.toRadians(0))
                .driveTo(GameField.leftSpike(cadbot).add(-5.0, 5.0), Math.toRadians(0)) // drive to spike
                .driveTo(GameField.centerOfTile(2, 5), Math.toRadians(0)) // backup
                .driveTo(GameField.centerOfTile(2,4), Math.toRadians(0)) // forward to the center
                .driveTo(GameField.centerOfTile(6, 4), Math.toRadians(0)) // forward down the center
//                .driveTo(GameField.redBackdrop(), Math.toRadians(0)) // to the backdrop
                .build();
    }

    public static ActionSequence rightSpike(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.rightSpike(cadbot).add(0, 5.0), Math.toRadians(-90)) // drive to spike
                .driveTo(GameField.betweenTiles(1, 6, 2, 6), Math.toRadians(-90)) // backup
                .driveTo(GameField.centerOfTile(1, 6), Math.toRadians(-90)) // strafe to wall
                .driveTo(GameField.centerOfTile(1, 4), Math.toRadians(-90))
                .rotateTo(GameField.centerOfTile(1, 4), Math.toRadians(0))
                .driveTo(GameField.centerOfTile(6, 4), Math.toRadians(0)) // forward down the center
//                .driveTo(GameField.redBackdrop(), Math.toRadians(0)) // to the backdrop
                .build();
    }

    public static ActionSequence middleSpike(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.centerSpike(cadbot).add(0, 5.0), Math.toRadians(-90)) // drive to spike
                .driveTo(GameField.centerOfTile(2,5), Math.toRadians(-90)) // backup
                .driveTo(GameField.centerOfTile(1, 5), Math.toRadians(-90)) // strafe toward wall
                .driveTo(GameField.centerOfTile(1,4), Math.toRadians(-90)) // forward to the center
                .rotateTo(GameField.centerOfTile(1,4), Math.toRadians(0)) // rotate to face the backdrop
                .driveTo(GameField.centerOfTile(6, 4), Math.toRadians(0)) // forward down the center
//                .driveTo(GameField.redBackdrop(), Math.toRadians(0)) // to the backdrop
                .build();
    }

}
