package org.firstinspires.ftc.teamcode.fuzzy;

public class MembershipFunction {
    private double min; // the smallest value for which this membership function applies
    private double max; // the largest value for which this membership function applies
    private double slope; // the slope of the function in this range
    private double b; // the intercept
    private SlopeType slopeType;

    public MembershipFunction(double min, double max, SlopeType slopeType){
        this.min = min;
        this.max = max;
        this.slopeType = slopeType;
        if(slopeType == SlopeType.POSITIVE){
            slope = 1/(max - min); // positive slope
            b = -slope * min + 0.0;
        }
        else if(slopeType == SlopeType.NEGATIVE){
            slope = 1/(min - max); // negative slope
            b = -slope * min + 1.0;
        }
        else{
            slope = 0;
            b = -slope * min + 1.0;
        }

    }

    // returns the degree of membership to this fuzzy category
    public double degree(double input){
        if(input < min)
            return 0.0;
        if(input > max)
            return 0.0;
        return slope * input + b;
    }
}
