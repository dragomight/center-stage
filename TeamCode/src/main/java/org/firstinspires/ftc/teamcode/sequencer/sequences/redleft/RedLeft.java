package org.firstinspires.ftc.teamcode.sequencer.sequences.redleft;

import org.firstinspires.ftc.teamcode.BillsTensorTunes.SpikeMark;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.sequencer.ActionSequence;
import org.firstinspires.ftc.teamcode.sequencer.GameField;
import org.firstinspires.ftc.teamcode.sequencer.SequenceBuilder;

public class RedLeft {
    public static ActionSequence start(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .driveTo(GameField.betweenTiles(2,2, 2, 1), Math.toRadians(90))
                .scanForSmartStout(SpikeMark.MIDDLE)
                .build();
    }
}
