package org.firstinspires.ftc.teamcode.BillsUtilityGarage;

public class Vector2D1 {
    private double x;
    private double y;
    private double heading; // an angle in radians ranging from -pi to pi
    private final static double TWO_PI = 2 * Math.PI;

    public Vector2D1(){

    }

    public Vector2D1(double x, double y, double heading){
        this.x = x;
        this.y = y;
        this.heading = (heading + Math.PI)%TWO_PI - Math.PI; // limits the number to the range -PI to PI
    }

    public Vector2D1(Vector2D v, double heading){
        this.x = v.getX();
        this.y = v.getY();
        this.heading = (heading + Math.PI)%TWO_PI - Math.PI; // limits the number to the range -PI to PI
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getHeading(){
        return heading;
    }

    /**
     * The purpose of this function is to determine the amount and direction needed to change from
     * the initial angle to the final angle.  This problem is complicated by the range being -PI to PI
     * When the difference is greater than PI, its easier to go the other way around.
     * @param thFinal
     * @param thInit
     * @return
     */
    public static double angularDifference(double thFinal, double thInit){
        double diff = thFinal - thInit;
        if(diff > Math.PI){ // .9pi - -.9pi = 1.8pi  is > pi
            return Math.PI - diff; // so we return -0.8 pi
        }
        if(diff < -Math.PI){  // -.9pi - .9pi = -1.8pi is < -pi
            return -Math.PI - diff; // so we return -pi - -1.8pi = .8pi
        }
        return diff;
    }

    public String toString(){
        return String.format("[%3f, %3f, %3f]", x, y, Math.toDegrees(heading));
    }
}
