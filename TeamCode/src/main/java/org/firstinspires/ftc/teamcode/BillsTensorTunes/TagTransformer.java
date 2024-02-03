package org.firstinspires.ftc.teamcode.BillsTensorTunes;

import android.util.Log;

import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D1;
import org.firstinspires.ftc.teamcode.sequencer.GameField;

public class TagTransformer {

    // neglecting z completely
    public static Vector2D1 robotPoseFromFrontCamera(int tagId, double tagX, double tagY, double tagYaw){

        // change the yaw to radians
        tagYaw = Math.toRadians(tagYaw);

        // transform from tag coordinates (camera coordinates) to our robot coordinates and add displacement of camera
        double toTagX = tagY + ThirdEyeSurfer.CAMERA_X;
        double toTagY = -tagX;

        // transform to world coordinates
        double wX = toTagX * Math.cos(tagYaw) - toTagY * Math.sin(tagYaw);
        double wY = toTagX * Math.sin(tagYaw) + toTagY * Math.cos(tagYaw);

        // use the tag id to get the world coordinates of the tag
        Vector2D1 tagLocation = GameField.tagLocation(tagId);

        // calculate the position of the robot in world coordinates
        double rX = tagLocation.getX() - wX;
        double rY = tagLocation.getY() - wY;

        Vector2D1 v = new Vector2D1(rX, rY, tagYaw);

        Log.e("TagTransformer", "tagId=" + tagId + "  x=" + tagX + "  y=" + tagY + " yaw=" + tagYaw + "  wX=" + wX + "  wY=" + wY + "  pose=" + v);

        return v;
    }

    public static Vector2D1 robotPoseFromBackCamera(int tagId, double tagX, double tagY, double tagYaw){
        // change the yaw to radians
        tagYaw = Math.toRadians(tagYaw);

        // transform from tag coordinates (camera coordinates) to our robot coordinates and add displacement of camera
        double toTagX = tagY + ThirdEyeSurfer.CAMERA_X;
        double toTagY = -tagX;

        // transform to world coordinates
        double wX = toTagX * Math.cos(tagYaw) - toTagY * Math.sin(tagYaw);
        double wY = toTagX * Math.sin(tagYaw) + toTagY * Math.cos(tagYaw);

        // use the tag id to get the world coordinates of the tag
        Vector2D1 tagLocation = GameField.tagLocation(tagId);

        // calculate the position of the robot in world coordinates
        double rX = tagLocation.getX() - wX;
        double rY = tagLocation.getY() - wY;

        return new Vector2D1(-rX, -rY, tagYaw);
    }
}
