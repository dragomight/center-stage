package org.firstinspires.ftc.teamcode.sequencer.actions;

public class PlaceOnBackdrop implements RobotAction{
    @Override
    public void update() {
        // strafe to position
        // approach close, slowly
        // extend arm to target grid location, if sensor available use it to detect the backdrop
        // gripper pushes a pixel off
        // retract arm
        // backup slightly

        // repeat for second pixel
    }

    @Override
    public boolean isDone() {
        return false;
    }
}
