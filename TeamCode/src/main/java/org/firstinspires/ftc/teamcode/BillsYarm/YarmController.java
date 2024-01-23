package org.firstinspires.ftc.teamcode.BillsYarm;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BillsSystemsForSpareChange.GamePadState;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;

public class YarmController {
    Vector2D point1;
    Vector2D point2;

    Telemetry telemetry;
    GamePadState gamePadState;
    Yarm yarm;

    private boolean stick;

    public void initialize(GamePadState gamePadState, Yarm yarm, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.gamePadState = gamePadState;
        this.yarm = yarm;

        point1 = new Vector2D();
        point2 = new Vector2D();
    }

    public void update(boolean verbose) {
        yarm.launch = gamePadState.y; // TODO: Make a new location for launcher control

        if (gamePadState.dPadUp) { // Ready position
            yarm.joint1Target = -20;
            yarm.joint2Target = 20;
        }

        if (gamePadState.dPadLeft) { // Pull
            yarm.joint1Target = 50;
            yarm.joint2Target = -50;
        }

        if (gamePadState.dPadDown) { // Rest (straight up)
            yarm.joint1Target = 0;
            yarm.joint2Target = 0;
        }

        //yarm.updateVelocities();

        if (verbose) {
            telemetry.addData("Target1 ", yarm.joint1Target);
            telemetry.addData("Target2 ", yarm.joint2Target);

            telemetry.addData("Target point: ", yarm.endPointTarget.toString());

            telemetry.addData("Endpoint: ", yarm.endpoint.toString());

            telemetry.addData("Point1: ", point1.toString());
            telemetry.addData("Point2: ", point2.toString());

            telemetry.addData("Arrival1: ", yarm.arrival1);
            telemetry.addData("Arrival2: ", yarm.arrival2);
        }
    }
}
