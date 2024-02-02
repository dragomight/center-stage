package org.firstinspires.ftc.teamcode.sequencer.sequences.armCalibration;

import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.sequencer.ActionSequence;
import org.firstinspires.ftc.teamcode.sequencer.SequenceBuilder;

public class GripperTests {

    // Run this test to check that both motors turn in the same pushing direction
    public static ActionSequence gripperTest1(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .gripperPush(1.0, true)
                .build();
    }

    // Run this test to check the motors pull a pixel on
    public static ActionSequence gripperTest2(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .gripperPush(1.0, false)
                .build();
    }

    // Run this test to alternate pushing and pulling to determine ideal length of time to
    // pull two and push one pixel.  RedLeft with an interval like 0.1 sec, increase and repeat
    public static ActionSequence gripperTest3(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .gripperPush(0.3, false) // solution: make the physical barrier allow only two pixels of space,
                .wait(1.0)
                .gripperPush(0.1, true) // then run the pull on for longer than the push off
                .wait(1.0)
                .gripperPush(0.1, true) // then optimize push off to only admit one at a time
                .wait(1.0)
                .gripperPush(0.2, false)
                .wait(1.0)
                .gripperPush(0.1, true)
                .wait(1.0)
                .gripperPush(0.1, true)
                .wait(1.0)
                .gripperPush(0.2, false)
                .wait(1.0)
                .gripperPush(0.1, true)
                .wait(1.0)
                .gripperPush(0.1, true)
                .wait(1.0)
                .gripperPush(0.2, false)
                .wait(1.0)
                .gripperPush(0.1, true)
                .wait(1.0)
                .gripperPush(0.1, true)
                .wait(1.0)
                .gripperPush(0.2, false)
                .wait(1.0)
                .gripperPush(0.1, true)
                .wait(1.0)
                .gripperPush(0.1, true)
                .wait(1.0)
                .gripperPush(0.2, false)
                .wait(1.0)
                .gripperPush(0.1, true)
                .wait(1.0)
                .gripperPush(0.1, true)
                .wait(1.0)
                .gripperPush(0.2, false)
                .wait(1.0)
                .gripperPush(0.1, true)
                .wait(1.0)
                .gripperPush(0.1, true)
                .wait(1.0)
                .gripperPush(0.2, false)
                .wait(1.0)
                .gripperPush(0.1, true)
                .wait(1.0)
                .gripperPush(0.1, true)
                .wait(1.0)
                .gripperPush(0.2, false)
                .wait(1.0)
                .gripperPush(0.1, true)
                .wait(1.0)
                .gripperPush(0.1, true)
                .wait(1.0)
                .build();
    }
}
