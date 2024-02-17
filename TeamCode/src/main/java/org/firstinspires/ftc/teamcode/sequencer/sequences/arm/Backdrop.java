package org.firstinspires.ftc.teamcode.sequencer.sequences.arm;

import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmConstants;
import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmPoseXZ;
import org.firstinspires.ftc.teamcode.BillsEs.AllianceColor;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector3D;
import org.firstinspires.ftc.teamcode.sequencer.engine.ActionSequence;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.GameField;
import org.firstinspires.ftc.teamcode.sequencer.engine.SequenceBuilder;

public class Backdrop {
    // strafe to position
    // approach close, slowly
    // extend arm to target grid location, if sensor available use it to detect the backdrop
    // gripper pushes a pixel off
    // retract arm
    // backup slightly

    // repeat for second pixel

    public static ActionSequence placePixel(Cadbot cadbot, int row, int col){

        Vector3D target = GameField.getBackdropPixelPosition(row, col);

        if(cadbot.allianceColor == AllianceColor.BLUE){
            return new SequenceBuilder(cadbot)
                    .scanForwardForLocation()
                    .driveTo(GameField.blueBackdrop().add(-ArmConstants.L3, target.y), 0)
                    .moveArmTo(ArmPoseXZ.ready2())
                    .moveArmTo(ArmPoseXZ.placeOnBackdrop(target.x, target.z))
                    .gripperPush(2.0, true)
                    .moveArmTo(ArmPoseXZ.ready2())
                    .moveArmTo(ArmPoseXZ.carry())
                    .build();
        }
        else {
            return new SequenceBuilder(cadbot)
                    .scanForwardForLocation()
                    .driveTo(GameField.redBackdrop().add(-ArmConstants.L3, target.y), 0)
                    .moveArmTo(ArmPoseXZ.ready2())
                    .moveArmTo(ArmPoseXZ.placeOnBackdrop(target.x, target.z))
                    .gripperPush(2.0, true)
                    .moveArmTo(ArmPoseXZ.ready2())
                    .moveArmTo(ArmPoseXZ.carry())
                    .build();
        }
    }
}
