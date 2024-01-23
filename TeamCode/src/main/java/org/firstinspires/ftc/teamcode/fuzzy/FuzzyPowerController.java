package org.firstinspires.ftc.teamcode.fuzzy;

import android.util.Log;

// This is an example fuzzy power controller made with FuzzyController2D
public class FuzzyPowerController {
    public static FuzzyController2D getPowerController(){
        // rules[speed][distance] are the power settings
        double[][] rules = {{0.0, 1.0, 1.0, 1.0, 1.0}, // very slow
                            {0.0, 1.0, 1.0, 1.0, 1.0}, // slow
                            {0.0, 0.0, 1.0, 1.0, 1.0}, // medium
                            {0.0, 0.0, 0.0, 1.0, 1.0}, // fast
                            {0.0, 0.0, 0.0, 0.0, 1.0}}; // very fast

        FuzzyDimension distance = new FuzzyDimension(); // 37 is fastest stopping distance
        distance.addCategory(new FuzzyCategory("very near", 0).addFunction(0.0, 6.0, SlopeType.NEGATIVE));
        distance.addCategory(new FuzzyCategory("near", 1).addFunction(0.0, 6.0, SlopeType.POSITIVE).addFunction(6.0, 12.0, SlopeType.NEGATIVE));
        distance.addCategory(new FuzzyCategory("middle", 2).addFunction(6.0, 12.0, SlopeType.POSITIVE).addFunction(12.0, 24.0, SlopeType.NEGATIVE));
        distance.addCategory(new FuzzyCategory("far", 3).addFunction(12.0, 24.0, SlopeType.POSITIVE).addFunction(24.0, 48.0, SlopeType.NEGATIVE));
        distance.addCategory(new FuzzyCategory("very far", 4).addFunction(24.0, 48.0, SlopeType.POSITIVE).addFunction(48.0, 500.0, SlopeType.FLAT));

        FuzzyDimension speed = new FuzzyDimension();
        speed.addCategory(new FuzzyCategory("very slow", 0).addFunction(0.0, 6.0, SlopeType.NEGATIVE));
        speed.addCategory(new FuzzyCategory("slow", 1).addFunction(0.0, 6.0, SlopeType.POSITIVE).addFunction(6.0, 12.0, SlopeType.NEGATIVE));
        speed.addCategory(new FuzzyCategory("medium", 2).addFunction(6.0, 12.0, SlopeType.POSITIVE).addFunction(12.0, 24.0, SlopeType.NEGATIVE));
        speed.addCategory(new FuzzyCategory("fast", 3).addFunction(12.0, 24.0, SlopeType.POSITIVE).addFunction(24.0, 48.0, SlopeType.NEGATIVE));
        speed.addCategory(new FuzzyCategory("very fast", 4).addFunction(24.0, 48.0, SlopeType.POSITIVE).addFunction(48.0, 500.0, SlopeType.FLAT));


        return new FuzzyController2D(rules, speed, distance);
    }

    public static void test(){
        FuzzyController2D controller = getPowerController();
        Log.e("FuzzyController:", "Running Test");
        double[] input = {0, 0};
        for(double i=0; i<10; i++) {
            for(double j=0; j<10; j++) {
                input[0] = i;
                input[1] = j;
                double p = controller.controlValue(input);
                Log.e("FuzzyController", "i=" + i + "   j=" + j + "   p=" + p);
            }
        }
    }
}
