package org.firstinspires.ftc.teamcode.fuzzy;

import java.util.ArrayList;

public class FuzzyCategory {

    private ArrayList<MembershipFunction> functions = new ArrayList<>();
    private double activationValue;
    private String name;
    private int id;

    public FuzzyCategory(String name, int id){
        this.name = name;
        this.id = id;
    }

    public FuzzyCategory addFunction(double min, double max, SlopeType slopeType){
        functions.add(new MembershipFunction(min, max, slopeType));
        return this;
    }

    public int getId(){
        return id;
    }

    public double activation(double input){
        activationValue = 0.0;
        for(MembershipFunction f: functions){
            activationValue = f.degree(input);
            if(activationValue != 0){
                return activationValue;
            }
        }
        return activationValue;
    }

    public double getActivationValue(){
        return activationValue;
    }

    public String getName(){
        return name;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(name + " " + id + " " + activationValue);
        return sb.toString();
    }
}
