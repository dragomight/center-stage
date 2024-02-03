package org.firstinspires.ftc.teamcode.sequencer;

import org.firstinspires.ftc.teamcode.BillsEs.AllianceColor;
import org.firstinspires.ftc.teamcode.BillsEs.AlliancePosition;
import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;
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


    public final static double BACKDROP_ANGLE = Math.toRadians(60.0); // angle from floor, can be 62.0
    public final static double COS_BDA = Math.cos(BACKDROP_ANGLE);
    public final static double SIN_BDA = Math.sin(BACKDROP_ANGLE);

    public final static double COL_SPACING = 3.0; // spacing of the pixel grid, x is left to right
    public final static double ROW_SPACING = 2.5; // spacing of the pixel grid, y is bottom to top
    public final static double HEIGHT_BOTTOM_ROW = 8.5; // in 11, 13.5,
    // pixel bottom row is 1-6, then 1-7, then 1-6, etc.

    /**
     *  Returns how far out, left, and up a pixel center is given its row and column numbers.
     *  row is numbered from bottom to top.
     *  col is numbered from left to right
     *  x has origin at the base of the board (this needs to be added to the distance from the robot to the base to be fully in robot coordinates)
     *  y has origin in the center of the board
     *  z has origin at the floor level
     */
    public static Vector3D getBackdropPixelPosition(double row, double col){
        double hyp = HEIGHT_BOTTOM_ROW + ROW_SPACING * row; // height along the grid surface (hypotenuse)
        double x = hyp * COS_BDA;
        double y = 0;
        // if row is odd (we start with row 1)
        if(row%2==1){
            y = COL_SPACING * (3.5-col); // where is origin of y is center of the board and +y is left
        }
        else{
            y = COL_SPACING * (4.0-col);
        }
        double z = hyp * SIN_BDA;
        return new Vector3D(x, y, z);
    }

    public static Vector2D redBackdrop(){
        return new Vector2D(TILE_SIZE * 2 + 3, -TILE_SIZE * 1.5);
    }

    public static Vector2D blueBackdrop(){
        return new Vector2D(TILE_SIZE * 2 + 3, TILE_SIZE * 1.5);
    }

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
    public static Vector2D centerOfTile(int x, int y){
        return new Vector2D((x-3)*TILE_SIZE - HALF_TILE_SIZE, (y-3)*TILE_SIZE - HALF_TILE_SIZE);
    }

    public static Vector2D betweenTiles(int xa, int ya, int xb, int yb){
        return centerOfTile(xa, ya).add(centerOfTile(xb, yb)).divideBy(2.0);
    }

    // center of the robot is 5" behind the pushed pixel
    public final static double PIXEL_DIST = 5.0;
    public static Vector2D leftSpike(Cadbot cadbot){
        if(cadbot.allianceColor == AllianceColor.RED){
            if(cadbot.alliancePosition == AlliancePosition.LEFT){
                return new Vector2D(-46.5, -30.0);
            }
            else{ // RIGHT
                return new Vector2D(1.0, -30.0);
            }
        }
        else{ // BLUE
            if(cadbot.alliancePosition == AlliancePosition.LEFT){
                return new Vector2D(24.0, 30.0);
            }
            else{ // RIGHT
                return new Vector2D(-24.0, 30.0);
            }
        }
    }

    public static Vector2D rightSpike(Cadbot cadbot){
        if(cadbot.allianceColor == AllianceColor.RED){
            if(cadbot.alliancePosition == AlliancePosition.LEFT){
                return new Vector2D(-24.5, -30.0);
            }
            else{ // RIGHT
                return new Vector2D(24.5, -30.0);
            }
        }
        else{ // BLUE
            if(cadbot.alliancePosition == AlliancePosition.LEFT){
                return new Vector2D(1.0, 30.0);
            }
            else{ // RIGHT
                return new Vector2D(-46.5, 30.0);
            }
        }
    }

    public static Vector2D centerSpike(Cadbot cadbot){
        if(cadbot.allianceColor == AllianceColor.RED){
            if(cadbot.alliancePosition == AlliancePosition.LEFT){
                return new Vector2D(-35.5, -24.5);
            }
            else{ // RIGHT
                return new Vector2D(12, -24.5);
            }
        }
        else{ // BLUE
            if(cadbot.alliancePosition == AlliancePosition.LEFT){
                return new Vector2D(12, 24.5);
            }
            else{ // RIGHT
                return new Vector2D(-35.5, 24.5);
            }
        }
    }
}
