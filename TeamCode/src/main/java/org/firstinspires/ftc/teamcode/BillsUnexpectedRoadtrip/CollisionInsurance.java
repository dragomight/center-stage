package org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip;

import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;

public class CollisionInsurance {
    /**
     * Takes the current pose and the delta of position to detect for collisions and returns
     * a corrected delta that avoids collisions.
     * @param tracker
     * @param delta
     * @return
     */
    public static Vector2D avoidCollisions(DeadWheelTracker tracker, Vector2D delta){

        // project the next position
        Vector2D nextPose = tracker.projectNextPose(delta);

        double dx = delta.getX();
        double dy = delta.getY();

        // first test for collisions with the field perimeter
        if(nextPose.getX() - DeadWheelTracker.COLLISION_RADIUS < -GameField.PERIMETER){
            if(dx < 0)
                dx = 0;
        }
        else if(nextPose.getX() + DeadWheelTracker.COLLISION_RADIUS > GameField.PERIMETER){
            if(dx > 0)
                dx = 0;
        }

        if(nextPose.getY() - DeadWheelTracker.COLLISION_RADIUS < -GameField.PERIMETER){
            if(dy < 0)
                dy = 0;
        }
        else if(nextPose.getY() + DeadWheelTracker.COLLISION_RADIUS > GameField.PERIMETER){
            if(dy > 0)
                dy = 0;
        }

        // test for collisions with the backdrop
        if(nextPose.getY() - DeadWheelTracker.COLLISION_RADIUS < GameField.TILE_SIZE * 2){
            if(nextPose.getY() + DeadWheelTracker.COLLISION_RADIUS > GameField.TILE_SIZE){
                if(nextPose.getX() + DeadWheelTracker.COLLISION_RADIUS > GameField.TILE_SIZE * 2.5){
                    if(dx > 0){
                        dx = 0;
                    }
                }
            }
        }

        if(nextPose.getY() - DeadWheelTracker.COLLISION_RADIUS < GameField.TILE_SIZE){
            if(nextPose.getY() + DeadWheelTracker.COLLISION_RADIUS > GameField.TILE_SIZE * 2){
                if(nextPose.getX() + DeadWheelTracker.COLLISION_RADIUS > GameField.TILE_SIZE * 2.5){
                    if(dx > 0){
                        dx = 0;
                    }
                }
            }
        }

        // test for collisions with other obstacles (poles)
        Vector2D del = avoidPole(GameField.TILE_SIZE * 0, GameField.TILE_SIZE * 1, nextPose, dx, dy);
        del = avoidPole(GameField.TILE_SIZE * 0, GameField.TILE_SIZE * 2, nextPose, del.getX(), del.getY());
        del = avoidPole(GameField.TILE_SIZE * 0, GameField.TILE_SIZE * 3, nextPose, del.getX(), del.getY());
        del = avoidPole(GameField.TILE_SIZE * 0, GameField.TILE_SIZE * -1, nextPose, del.getX(), del.getY());
        del = avoidPole(GameField.TILE_SIZE * 0, GameField.TILE_SIZE * -2, nextPose, del.getX(), del.getY());
        del = avoidPole(GameField.TILE_SIZE * 0, GameField.TILE_SIZE * -3, nextPose, del.getX(), del.getY());

        del = avoidPole(GameField.TILE_SIZE * -1, GameField.TILE_SIZE * 1, nextPose, del.getX(), del.getY());
        del = avoidPole(GameField.TILE_SIZE * -1, GameField.TILE_SIZE * 2, nextPose, del.getX(), del.getY());
        del = avoidPole(GameField.TILE_SIZE * -1, GameField.TILE_SIZE * 3, nextPose, del.getX(), del.getY());
        del = avoidPole(GameField.TILE_SIZE * -1, GameField.TILE_SIZE * -1, nextPose, del.getX(), del.getY());
        del = avoidPole(GameField.TILE_SIZE * -1, GameField.TILE_SIZE * -2, nextPose, del.getX(), del.getY());
        del = avoidPole(GameField.TILE_SIZE * -1, GameField.TILE_SIZE * -3, nextPose, del.getX(), del.getY());

        return new Vector2D(del.getX(), del.getY());
    }

    private static Vector2D avoidPole(double x, double y, Vector2D nextPose, double dx, double dy){
        if(nextPose.getX() - DeadWheelTracker.COLLISION_RADIUS < x) {
            if (x < nextPose.getX() + DeadWheelTracker.COLLISION_RADIUS) {
                if (nextPose.getY() - DeadWheelTracker.COLLISION_RADIUS < y) {
                    if (y < nextPose.getY() + DeadWheelTracker.COLLISION_RADIUS) {
                        // there is a potential collisions
                        if (x > nextPose.getX()) {
                            if (dx > 0) {
                                dx = 0;
                            }
                        } else {
                            if (dx < 0) {
                                dx = 0;
                            }
                        }
                        if (y > nextPose.getY()) {
                            if (dy > 0) {
                                dy = 0;
                            }
                        } else {
                            if (dy < 0) {
                                dy = 0;
                            }
                        }
                    }
                }
            }
        }
        return new Vector2D(dx, dy);
    }
}
