package org.firstinspires.ftc.teamcode.sequencer.sequences.autonomous;

import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.sequencer.engine.ActionSequence;
import org.firstinspires.ftc.teamcode.sequencer.engine.GameField;
import org.firstinspires.ftc.teamcode.sequencer.engine.SequenceBuilder;

public class RedRight {
    public static ActionSequence start(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.betweenTiles(4,2, 4, 1), Math.toRadians(90))
                .scanForSmartStout()
                .build();
    }

    public static ActionSequence leftSpike(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.centerOfTile(4, 2), Math.toRadians(90))
                .rotateTo(GameField.centerOfTile(4, 2), Math.toRadians(180))
                .driveTo(GameField.leftSpike(cadbot).add(6, -5.0), Math.toRadians(180))
                .driveTo(GameField.centerOfTile(5, 2), Math.toRadians(180))
                .rotateTo(GameField.centerOfTile(5, 2), Math.toRadians(0))
                .driveTo(GameField.centerOfTile(5, 1), Math.toRadians(0)) // park
                .driveTo(GameField.centerOfTile(6, 1), Math.toRadians(0)) // park
                .gripperPush(2.0, true)
                .driveTo(GameField.centerOfTile(6, 1).add(-3,0), Math.toRadians(0)) // park
                .build();
    }

    public static ActionSequence rightSpike(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.rightSpike(cadbot).add(0, -5.0), Math.toRadians(90)) // drive to spike
                .driveTo(GameField.betweenTiles(4, 1, 5, 1), Math.toRadians(90)) // backup
                .rotateTo(GameField.betweenTiles(4, 1, 5, 1), Math.toRadians(0)) // turn to board
                .driveTo(GameField.centerOfTile(6, 1), Math.toRadians(0)) // park
                .gripperPush(2.0, true)
                .driveTo(GameField.centerOfTile(6, 1).add(-3,0), Math.toRadians(0)) // park
                .build();
    }

    public static ActionSequence middleSpike(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.centerSpike(cadbot).add(0, -5.0), Math.toRadians(90)) // drive to spike
                .driveTo(GameField.centerOfTile(4, 1), Math.toRadians(90)) // backup
                .rotateTo(GameField.centerOfTile(4, 1), Math.toRadians(0)) // turn to board
                .driveTo(GameField.centerOfTile(6, 1), Math.toRadians(0)) // park
                .gripperPush(2.0, true)
                .driveTo(GameField.centerOfTile(6, 1).add(-3,0), Math.toRadians(0)) // park
                .build();
    }


}
