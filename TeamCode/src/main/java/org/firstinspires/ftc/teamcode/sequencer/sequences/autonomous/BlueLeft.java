package org.firstinspires.ftc.teamcode.sequencer.sequences.autonomous;

import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.sequencer.ActionSequence;
import org.firstinspires.ftc.teamcode.sequencer.GameField;
import org.firstinspires.ftc.teamcode.sequencer.SequenceBuilder;

public class BlueLeft {
    public static ActionSequence start(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.betweenTiles(4,6, 4, 5), Math.toRadians(-90))
                .scanForSmartStout()
                .build();
    }

    public static ActionSequence leftSpike(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.leftSpike(cadbot).add(0, 5.0), Math.toRadians(-90)) // drive to spike
                .driveTo(GameField.betweenTiles(4, 6, 5, 6), Math.toRadians(-90)) // backup
                .rotateTo(GameField.betweenTiles(4, 6, 5, 6), Math.toRadians(0)) // rotate to face the backdrop
                .driveTo(GameField.centerOfTile(6, 6), Math.toRadians(0)) // park
//                .driveTo(GameField.redBackdrop(), Math.toRadians(0)) // to the backdrop
                .build();
    }

    public static ActionSequence rightSpike(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.centerOfTile(4, 5), Math.toRadians(-90)) // position
                .rotateTo(GameField.centerOfTile(4, 5), Math.toRadians(-180)) // rotate
                .driveTo(GameField.rightSpike(cadbot).add(5, 5), Math.toRadians(-180)) // place
                .driveTo(GameField.centerOfTile(5, 5), Math.toRadians(-180)) // backup
                .rotateTo(GameField.centerOfTile(5,5), Math.toRadians(0)) // rotate to face the backdrop
                .driveTo(GameField.centerOfTile(5, 6), Math.toRadians(0)) // park
                .driveTo(GameField.centerOfTile(6, 6), Math.toRadians(0)) // park
//                .driveTo(GameField.redBackdrop(), Math.toRadians(0)) // to the backdrop
                .build();
    }

    public static ActionSequence middleSpike(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.centerSpike(cadbot).add(0, 5.0), Math.toRadians(-90)) // drive to spike
                .driveTo(GameField.centerOfTile(4, 6), Math.toRadians(-90)) // backup
                .rotateTo(GameField.centerOfTile(4,6), Math.toRadians(0)) // rotate to face the backdrop
                .driveTo(GameField.centerOfTile(6, 6), Math.toRadians(0)) // park
//                .driveTo(GameField.redBackdrop(), Math.toRadians(0)) // to the backdrop
                .build();
    }


}
