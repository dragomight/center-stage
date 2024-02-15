package org.firstinspires.ftc.teamcode.sequencer.sequences.arm;

import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmPoseXZ;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.sequencer.ActionSequence;
import org.firstinspires.ftc.teamcode.sequencer.SequenceBuilder;

public class Backdrop {
    // strafe to position
    // approach close, slowly
    // extend arm to target grid location, if sensor available use it to detect the backdrop
    // gripper pushes a pixel off
    // retract arm
    // backup slightly

    // repeat for second pixel

    public static ActionSequence placePixel(Cadbot cadbot, int row, int col){
        return new SequenceBuilder(cadbot)
                .moveArmTo(ArmPoseXZ.home())
                .moveArmTo(ArmPoseXZ.carry())
                .moveArmTo(ArmPoseXZ.ready2())
                //.moveArmTo(ArmPoseXZ.forward())
                //.moveArmTo(ArmPoseXZ.passingOverForward())
                .moveArmTo(ArmPoseXZ.reachingForward(12, 8.0))
                .moveArmTo(ArmPoseXZ.reachingForward(12, 0.0))
                .wait(1.0)
                .gripperPush(3.0, false)
                .moveArmTo(ArmPoseXZ.reachingForward(12, 8.0))
                //.moveArmTo(ArmPoseXZ.passingOverForward())
                .moveArmTo(ArmPoseXZ.ready2())
                .moveArmTo(ArmPoseXZ.carry())
                .build();
    }
}
