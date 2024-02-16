package org.firstinspires.ftc.teamcode.sequencer.sequences.arm;

import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmConstants;
import org.firstinspires.ftc.teamcode.BillsAmazingArm.ArmPoseXZ;
import org.firstinspires.ftc.teamcode.BillsEs.AllianceColor;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D1;
import org.firstinspires.ftc.teamcode.sequencer.engine.ActionSequence;
import org.firstinspires.ftc.teamcode.sequencer.engine.GameField;
import org.firstinspires.ftc.teamcode.sequencer.engine.SequenceBuilder;

public class TrussMeImAnEngineer {
    public static ActionSequence hangingMyself(Cadbot cadbot) {

        // get the position of the robot
        Vector2D1 pose = cadbot.deadWheelTracker.getPose();

        // determine the hanging pose and heading
        Vector2D hangingPose = new Vector2D();
        Vector2D readyPose = new Vector2D();
        double hangingHeading = 0;

        double xOffset = 3.75/2.0 + ArmConstants.L0x + .75;

        // hanging at blue
        if(pose.getY() > GameField.TILE_SIZE * 2){
            readyPose.set(-GameField.TILE_SIZE * 1.5, GameField.TILE_SIZE * 2.5);
            hangingPose.set(-xOffset, GameField.TILE_SIZE * 2.5);
            hangingHeading = 0;
        }
        else if(pose.getY() > GameField.TILE_SIZE){
            readyPose.set(GameField.TILE_SIZE * 1.5, GameField.TILE_SIZE * 1.5);
            hangingPose.set(xOffset, GameField.TILE_SIZE * 1.5);
            hangingHeading = Math.toRadians(180);
        }
        // hanging at red
        else if(pose.getY() < GameField.TILE_SIZE * 2){
            readyPose.set(-GameField.TILE_SIZE * 1.5, GameField.TILE_SIZE * 2.5);
            hangingPose.set(-xOffset, GameField.TILE_SIZE * 2.5);
            hangingHeading = 0;
        }
        else if(pose.getY() < GameField.TILE_SIZE){
            readyPose.set(GameField.TILE_SIZE * 1.5, GameField.TILE_SIZE * 1.5);
            hangingPose.set(xOffset, GameField.TILE_SIZE * 1.5);
            hangingHeading = Math.toRadians(180);
        }
        else{
            // if we didn't start in a correct location, just skip it
            return null;
        }

        return new SequenceBuilder(cadbot)
                .driveTo(readyPose, pose.getHeading())
                .rotateTo(readyPose, hangingHeading)
                .moveArmTo(ArmPoseXZ.straightUp())
                .driveTo(hangingPose, hangingHeading)
                .moveArmTo(ArmPoseXZ.hanging())
                .build();
    }
}
