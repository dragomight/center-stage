package org.firstinspires.ftc.teamcode.BillsAmazingArm;

import android.util.Log;

import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UnitOfAngle;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.UtilityKit;
import org.firstinspires.ftc.teamcode.BillsUtilityGarage.Vector2D;

import java.util.ArrayList;

public class InverseKinematics{

    /**
     * The central function of IK is to calculate the angular velocity of the base and second joints
     * that are required to a target position and target velocity of the end point of the second
     * arm segment.  L1 and L2 are the lengths of the two arm segments.
     * In the IK coordinates, x is out and y is up.
     */
    public static Vector2D thDot(double l1, double l2, double r, double z, double rDot, double zDot, double th1, double th2, UnitOfAngle unit) {

        double v = UtilityKit.cos(th2, unit);
        double vDot = (z*zDot + r*rDot)/(l1*l2);

        double q2 = th2; //UtilityKit.acos(v, unit);

        double q2Dot = -vDot/Math.sqrt(1.00001-(v*v)) * Math.signum(q2);

        double l2SinQ2 = l2 * UtilityKit.sin(q2, unit); // b
        double l2CosQ2 = l2 * UtilityKit.cos(q2, unit); // c
        double base = (l1 + l2CosQ2);

        //double u = UtilityKit.tan(g-th1, unit); //
        double u = l2SinQ2/(l1+l2CosQ2);
        //double uDot = (q2Dot*l2SinQ2*l2SinQ2)/(l1+l2CosQ2)*(l1+l2CosQ2) + (q2Dot*l2CosQ2)/(l1+l2CosQ2);
        double uDot = q2Dot * l2SinQ2 * l2SinQ2 / (base * base) + q2Dot * l2CosQ2 / base;

        double q1 = th1; //g - UtilityKit.atan2(l2SinQ2, l1+l2CosQ2, unit);
        double q1Dot = (rDot*z - r*zDot)/(z*z + r*r + 0.00001) - uDot/(1.0 + u*u);
        return new Vector2D(q1Dot, q2Dot);
    }

    // We made this work by reversing the x and y in atan2 function calls, but why?
    // x is the distance out in front of the robot
    // z is the height above the floor
    public static ArrayList<Vector2D> solveForTheta(double l1, double l2, double x, double z){
        ArrayList<Vector2D> solutions = new ArrayList<>();
        double c2 = (x*x + z*z - l1*l1 - l2*l2)/(2 * l1 * l2);
        // if the arm cannot reach this point because it is ether too near or too far,
//        if(Math.abs(c2)>1) {
//            Log.e("InverseKinematics", "No solution" + " l1=" + l1 + " l2=" + l2 + " x=" + x + " z=" + z + " c2=" + c2);
//            return solutions; // return no solution
//        }
        // if the arm is at its maximum reach, it is straight at j2, thus th2=0
        if(c2 >= 1){
            solutions.add(new Vector2D(Math.atan2(x, z), 0));
            return solutions; // return the extreme far reach solution
        }
        // if the arm is reaching back toward j1, with both segments aligned, then th2=PI
        if(c2 <= -1) {
            solutions.add(new Vector2D(Math.atan2(x, z), Math.PI));
            return solutions; // return the extreme close reach solution
        }

        // There remain two possible solutions (elbow up and elbow down) that can both reach (x, z)
        double q2 = Math.acos(c2);
        double q1 = Math.atan2(x, z) - Math.atan2(l2 * Math.sin(q2), l1 + l2 * Math.cos(q2));
        double q2b = -q2;
        double q1b = Math.atan2(x, z) - Math.atan2(l2 * Math.sin(q2b), l1 + l2 * Math.cos(q2b));
//        Log.e("InverseKinematics", "qb=" + q1b + " " + q2b);
        solutions.add(new Vector2D(q1, q2));
        solutions.add(new Vector2D(q1b, q2b));
        return solutions; // return both solutions
    }

    // given a finger tip point in (x, z) and th3, calculate the distance from j2 to tip,
    // and the angle of this line relative to j2.  This imaginary arm segment may then be used
    // to calculate the inverse kinematics for th1, th2i, which may then be used to get th2.
    public static Vector2D thetaExtended(double l1, double l2, double l3, double x, double z, double th3){

        // Use law of cosines to get the length of imaginary segment length l2i
        // if th3 < 0 then use -180 - th3
        double l2i = Math.sqrt(l2 * l2 + l3 * l3 - 2 * l2 * l3 * Math.cos(Math.PI - th3)); // todo

        // Calculate the angle offset of th2i, the imaginary joint angle, relative to j2, using the law of sines
        double th2offset = Math.asin( Math.sin(Math.PI - th3) * l3 / l2i ); // th2i will have the same sign as th3

        // calculate the IK for the imaginary segment and segment 1
        ArrayList<Vector2D> solutions = solveForTheta(l1, l2i, x, z);
        if(solutions.isEmpty()){
            Log.e("InverseKinematics", "No solution error.");
            return new Vector2D(); // todo: should return something we can handle
        }

        // use the first solution when th3 >= 0, and the second otherwise
        Vector2D theta;
        if(th3 >= 0) {
            theta = solutions.get(0).subtract(0, th2offset);
        }
        else{
            theta = solutions.get(1).subtract(0, th2offset);
        }

        Log.e("InverseKinematics", "l2i=" + l2i + " th2offset=" + th2offset + " theta=" + theta);

        return theta;
    }

    // This function serves as a test of the class and use case example
    public static void test(){
        double th1 = Math.toRadians(0);
        double th2 = Math.toRadians(0);
        Vector2D original = new Vector2D(th1, th2);

        // calculate the end point in x, y
        Vector2D pose = Kinematics.j3(new ArmPose(th1, th2, 0, 0));

        // remove the shift of the base joint
        Vector2D copyPose = pose.copy().subtract(ArmConstants.L0x, ArmConstants.L0z);

        ArrayList<Vector2D> results = InverseKinematics.solveForTheta(ArmConstants.L1, ArmConstants.L2, copyPose.getX(), copyPose.getY());
        if(results.isEmpty()){
            Log.e("InverseKinematics", "No results in test.");
        }
        else if(results.size() > 1){
            Log.e("InverseKinematics", "original=" + original + " copyPose=" + copyPose + " extra-crispy=" + results.get(0));
            Log.e("InverseKinematics", "original=" + original + " copyPose=" + copyPose + " extra-crispy=" + results.get(1));
        }
        else {
            Log.e("InverseKinematics", "original=" + original + " copyPose=" + copyPose + " extra-crispy=" + results.get(0));
        }
    }

    public static void test2(){
        Log.e("InverseKinematics", ArmConstants.string());
        double th1 = Math.toRadians(0);
        double th2 = Math.toRadians(0);
        double th3 = Math.toRadians(-90);
        Vector2D original = new Vector2D(th1, th2);

        // calculate the end point in x, y
        Vector2D tip = Kinematics.tip(new ArmPose(th1, th2, th3, 0));

        // remove the shift of the base joint
        Vector2D tipShifted = tip.copy().subtract(ArmConstants.L0x, ArmConstants.L0z);

        Vector2D result = InverseKinematics.thetaExtended(ArmConstants.L1,
                                                        ArmConstants.L2,
                                                        ArmConstants.L3,
                                                        tipShifted.getX(),
                                                        tipShifted.getY(),
                                                        th3);

        Log.e("InverseKinematics", "original=" + original + " tip=" + tip + " tipMod=" + tipShifted + " extra-crispy=" + result);

        th1 = Math.toRadians(0);
        th2 = Math.toRadians(0);
        th3 = Math.toRadians(90);
        original = new Vector2D(th1, th2);

        // calculate the end point in x, z
        tip = Kinematics.tip(new ArmPose(th1, th2, th3, 0));

        // remove the shift of the base joint
        tipShifted = tip.copy().subtract(ArmConstants.L0x, ArmConstants.L0z);

        result = InverseKinematics.thetaExtended(
                ArmConstants.L1,
                ArmConstants.L2,
                ArmConstants.L3,
                tipShifted.getX(),
                tipShifted.getY(),
                th3);

        Log.e("InverseKinematics", "original=" + original + " tip=" + tip + " tipMod=" + tipShifted + " extra-crispy=" + result);
    }
}
