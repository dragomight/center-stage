package org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.BillsAllyMap.BackMap;

/**
 * This class processes the control selections of the Game Pad Controller of Driver 2
 */
public class GamePadController2 {
    boolean lastRB = false;
    boolean lastUp = false;
    boolean lastDown = false;
    boolean lastLeft = false;
    boolean lastRight = false;

    Gamepad gamepad1;
    Gamepad gamepad2;
    Cadbot cadbot;

    BackMap backMap = new BackMap();

    public void initialize(Gamepad gamepad1, Gamepad gamepad2, Cadbot cadbot){
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.cadbot = cadbot;
    }

    public void update() {
        // SELECT THE PLACEMENT MODE
        if(gamepad2.x){
            backMap.setFirstCell();
        }
        if (gamepad2.y){
            backMap.setSecondCell();
        }
        if (gamepad2.b) {
            backMap.clearCell();
        }
        if (gamepad2.right_bumper) {
            backMap.fillSelectedCells();
            //cadbot.telemetry.addData("HIT ", "!!!");
        }

        if (gamepad1.right_bumper && !lastRB) {
            cadbot.autoPilot.placeOnBackdrop(backMap.firstSlot, backMap.secondSlot);
            cadbot.telemetry.addData("HIT ", "!!!");
        }
        lastRB = gamepad1.right_bumper;

        // CHANGE THE ROW AND COLUMN BEING SELECTED
        if(gamepad2.dpad_up && !lastUp){
            backMap.navigator.navUp();
        }
        lastUp = gamepad2.dpad_up;
        if(gamepad2.dpad_down && !lastDown){
            backMap.navigator.navDown();
        }
        lastDown = gamepad2.dpad_down;
        if(gamepad2.dpad_left && !lastLeft){
            backMap.navigator.navLeft();
        }
        lastLeft = gamepad2.dpad_left;
        if(gamepad2.dpad_right && !lastRight){
            backMap.navigator.navRight();
        }
        lastRight = gamepad2.dpad_right;

        // PLACE ON THE BACKDROP (executes an automated placement on the backdrop)
        if(gamepad2.b){
            // if either setting is outside of the allowed backdrop position it is not placed

        }
        cadbot.telemetry.addData("First ", backMap.firstSlot.selected);
        cadbot.telemetry.addData("Second ", backMap.secondSlot.selected);
        cadbot.telemetry.addData("Cell ", backMap.getCurrentCell().toString());
        cadbot.telemetry.addData(backMap.printMap(), backMap.printSong());
    }
}
