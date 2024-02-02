package org.firstinspires.ftc.teamcode.sequencer.sequences.armCalibration;

import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmPoseXZ;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.sequencer.ActionSequence;
import org.firstinspires.ftc.teamcode.sequencer.SequenceBuilder;

public class ArmTests {

    // First, set motor power to 0 and read the motion telemetry while moving it to various positions
    // After passing this inspection, set the motor power to about .3, extending the arm manually to straight up,
    // reset the start position to match what is below
    // and then run this test to hold the arm in the described position.
    public static ActionSequence armTest1(Cadbot cadbot){
        return new SequenceBuilder(cadbot)
                .moveArmTo(ArmPoseXZ.ready())
                .build();
    }

    // test that all motors run in the correct direction

    // test that it can move from home to straight up
}
