package org.firstinspires.ftc.teamcode.fuzzy;

import android.util.Log;

import java.util.ArrayList;

// This is not very generic because we are just testing it out.  It has only two inputs.
public class FuzzyController2D {

    private ArrayList<FuzzyDimension> dimensions = new ArrayList<>(); // lets just do two dimensions
    private FuzzyRules fuzzyRules;

    public FuzzyController2D(double[][] rules, FuzzyDimension d1, FuzzyDimension d2){
        fuzzyRules = new FuzzyRules(rules);
        dimensions.add(d1);
        dimensions.add(d2);
    }

    // here we use x for distance, and v for speed
    public double controlValue(double[] input){
        int i=0;
        // for each dimension
        for(FuzzyDimension d: dimensions) {

            // find active categories
            try {
                d.findActiveCategories(input[i++]);
            }
            catch (Exception e){
                Log.e("FuzzyController", e.toString());
            }
        }

        // there will be 2^n rules activated, where n is the number of input dimensions,
        // lookup the output value of the activated rules and perform a weighted overage of the stimulation levels for each
        double sum = 0;
        double divisor = 0;

        // get dimensions
        FuzzyDimension d0 = dimensions.get(0);
        FuzzyDimension d1 = dimensions.get(1);

        // for each active category in d0
        for(FuzzyCategory cat0: d0.activeCats) {
            for(FuzzyCategory cat1: d1.activeCats) {
                //Log.e("FuzzyController", cat0.toString() + "   " + cat1.toString());
                try {
                    sum += fuzzyRules.getRuleValue(cat0.getId(), cat1.getId()) * cat0.getActivationValue() * cat1.getActivationValue();
                    divisor += cat0.getActivationValue() * cat1.getActivationValue();
                }
                catch (Exception e){
                    Log.e("FuzzyController", e.toString());
                }
            }
        }

        return sum/divisor;
    }
}
