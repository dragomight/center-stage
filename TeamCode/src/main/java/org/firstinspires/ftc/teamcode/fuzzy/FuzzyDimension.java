package org.firstinspires.ftc.teamcode.fuzzy;

import java.util.ArrayList;

public class FuzzyDimension {
    ArrayList<FuzzyCategory> categories = new ArrayList<>();
    ArrayList<FuzzyCategory>  activeCats = new ArrayList<>();

    public void addCategory(FuzzyCategory cat){
        categories.add(cat);
    }

    public ArrayList<FuzzyCategory> findActiveCategories(double input){

        activeCats.clear();

        // for each membership function
        for(FuzzyCategory c: categories) {

            // determine the level of activation for this dimensions input
            double a = c.activation(input);

            // if the activation is non-zero, store the category
            if(a != 0){
                activeCats.add(c);
            }

            // (optional) when two are found for each dimension we can stop testing
        }

        return activeCats;
    }
}
