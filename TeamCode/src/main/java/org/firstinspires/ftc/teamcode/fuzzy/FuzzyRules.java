package org.firstinspires.ftc.teamcode.fuzzy;

public class FuzzyRules {

    double[][] rules;

    public FuzzyRules(double[][] rules){
        this.rules = rules;
    }

    public double getRuleValue(int id1, int id2){
        return rules[id1][id2];
    }
}
