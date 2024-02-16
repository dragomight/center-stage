package org.firstinspires.ftc.teamcode.sequencer.sequences.arm;

import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmPoseXZ;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.sequencer.engine.ActionSequence;
import org.firstinspires.ftc.teamcode.sequencer.engine.SequenceBuilder;

public class PixelPicker {
    public static ActionSequence grab(Cadbot cadbot){
        return new SequenceBuilder(cadbot)  // todo: add camera read for position of the pixel
                .moveArmTo(ArmPoseXZ.carry())
                .moveArmTo(ArmPoseXZ.ready2())
                //.moveArmTo(ArmPoseXZ.forward())
                //.moveArmTo(ArmPoseXZ.passingOverForward())
                .moveArmTo(ArmPoseXZ.reachingBackward(-12, 8.0))
                .moveArmTo(ArmPoseXZ.reachingBackward(-12, 0.0))
                .wait(1.0)
                .gripperPush(3.0, false)
                .moveArmTo(ArmPoseXZ.reachingBackward(-12, 8.0))
                //.moveArmTo(ArmPoseXZ.passingOverForward())
                .moveArmTo(ArmPoseXZ.ready2())
                .moveArmTo(ArmPoseXZ.carry())
                .build();
    }
}
