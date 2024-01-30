package org.firstinspires.ftc.teamcode.sequencer;

import org.firstinspires.ftc.teamcode.BillsEs.AllianceColor;
import org.firstinspires.ftc.teamcode.BillsEs.AlliancePosition;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D1;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector3D;

/**
 * This class provides transformations between robot and field coordinates.
 *
 * Field coordinates are defined for FTC with the origin at the center of the field.
 * The x-axis is pointed toward the backdrop, away from the audience.
 * The y-axis is pointed from the red side to the blue side.
 * The z-axis is up.
 *
 * Robot coordinates move with the robot.
 * The x-axis is forward.
 * The y-axis is left.
 * The z-axis is up.
 *
 * The facing of the robot is given as an angle relative to the x-axis with positive in the
 * clockwise direction.
 *
 * The transformation is complicated by the choice of starting position and orientation of
 * your robot in field coordinates.
 *
 * Instantiate the class with one of the constructors to indicate the starting conditions.
 *
 */
public class GameField {

    public final static double TILE_SIZE = 23.75; // inches
    public final static double HALF_TILE_SIZE = TILE_SIZE/2.0; // in

    // positions in front of the back drop
    public final static Vector2D BLUE_BACKDROP = new Vector2D(TILE_SIZE * 2 + 4, -TILE_SIZE * 1.5);
    public final static Vector2D RED_BACKDROP = new Vector2D(TILE_SIZE * 2 + 4, TILE_SIZE * 1.5);

    public final static double GRID_X = 3.0; // spacing of the pixel grid, x is up from bottom
    public final static double GRID_Y = 3.0; // spacing of the pixel grid, y is right from left most 1-6, then 1-7, then 1-6, etc.

    /**
     * Forward offset is the distance from the back of the robot to its center, and should be positive.
     * Robot is assumed to be centered on the start tile with the back flush against the wall.
     * @param color
     * @param position
     * @param forwardOffset
     */
    public static Vector2D1 getStartPose(AllianceColor color, AlliancePosition position, double forwardOffset){
        double xStart, yStart, fStart; // f is for facing

        if(color == AllianceColor.RED){
            if(position == AlliancePosition.LEFT){
                xStart = -1.5 * TILE_SIZE;
                yStart = -3.0 * TILE_SIZE + forwardOffset;
                fStart = Math.toRadians(90);
            }
            else{
                xStart = 0.5 * TILE_SIZE;
                yStart = -3.0 * TILE_SIZE + forwardOffset;
                fStart = Math.toRadians(90);
            }
        }
        else {
            if(position == AlliancePosition.LEFT){
                xStart = 0.5 * TILE_SIZE;
                yStart = 3.0 * TILE_SIZE - forwardOffset;
                fStart = Math.toRadians(-90);
            }
            else{
                xStart = -1.5 * TILE_SIZE;
                yStart = 3.0 * TILE_SIZE - forwardOffset;
                fStart = Math.toRadians(-90);
            }
        }
        return new Vector2D1(xStart, yStart, fStart);
    }

    /**
     * Transforms a point and bearing from robot coordinates (as the robot sees it) to field coords
     * @param pose
     * @param robotPose
     */
    public static Vector2D1 robotToField(Vector2D1 pose, Vector2D1 robotPose){
        double x = pose.getX() * Math.cos(robotPose.getHeading()) - pose.getY() * Math.sin(robotPose.getHeading());
        double y = pose.getY() * Math.cos(robotPose.getHeading()) + pose.getX() * Math.sin(robotPose.getHeading());
        double h = pose.getHeading() + robotPose.getHeading();
        return new Vector2D1(x, y, h);
    }

    /**
     * Transforms from field coordinates to robot coordinates.
     * For a 90 deg rotation:
     *  +x -> -y
     *  +y -> +x
     *  -y -> -x
     *  -x -> y
     * @param pose
     * @param heading
     */
    public static Vector2D fieldToRobot(Vector2D pose, double heading){
        double x = pose.getX() * Math.cos(heading) + pose.getY() * Math.sin(heading);
        double y = pose.getY() * Math.cos(heading) - pose.getX() * Math.sin(heading);
        return new Vector2D(x, y);
    }

    /**
     * Returns the tag pose in field coordinates.  Warning: the tilted tags may not provide correct information.
     * @param tagId
     * @return
     */
    public static Vector2D1 tagLocation(int tagId){
        switch (tagId){
            case 1: return new Vector2D1(3*TILE_SIZE - 9.0, TILE_SIZE + 18.0, Math.toRadians(180.0));
            case 2: return new Vector2D1(3*TILE_SIZE - 9.0, TILE_SIZE + 12.0, Math.toRadians(180.0));
            case 3: return new Vector2D1(3*TILE_SIZE - 9.0, TILE_SIZE + 6.0, Math.toRadians(180.0));
            case 4: return new Vector2D1(3*TILE_SIZE - 9.0, -TILE_SIZE - 6.0, Math.toRadians(180.0));
            case 5: return new Vector2D1(3*TILE_SIZE - 9.0, -TILE_SIZE - 12.0, Math.toRadians(180.0));
            case 6: return new Vector2D1(3*TILE_SIZE - 9.0, -TILE_SIZE - 18.0, Math.toRadians(180.0));
            case 7: return new Vector2D1(-3*TILE_SIZE, -TILE_SIZE - 12.0 - 5.5, 0.0);
            case 8: return new Vector2D1(-3*TILE_SIZE, -TILE_SIZE - 12.0, 0.0);
            case 9: return new Vector2D1(-3*TILE_SIZE, TILE_SIZE + 12.0, 0.0);
            case 10: return new Vector2D1(-3*TILE_SIZE, TILE_SIZE + 12.0 + 5.5, 0.0);
            default: return new Vector2D1();
        }
    }

    /**
     *              BLUE
     *                        y
     *                       /\
     *                       | (4, 6) (5, 6) (6, 6)
     *                       | (4, 5) (5, 5) (6, 5)
     *                       | (4, 4) (5, 4) (6, 4)
     * ---------------------------------->  x
     *  (1, 3) (2, 3) (3, 3) |
     *  (1, 2) (2, 2) (3, 2) |
     *  (1, 1) (2, 1) (3, 1) |
     *
     *              RED
     *
     * This function returns the x, y field coordinates of the center of a tile specified as above.
     * @param x
     * @param y
     * @return
     */
    public Vector2D centerOfTile(int x, int y){
        return new Vector2D((x-3)*TILE_SIZE - HALF_TILE_SIZE, (y-3)*TILE_SIZE - HALF_TILE_SIZE);
    }
}
