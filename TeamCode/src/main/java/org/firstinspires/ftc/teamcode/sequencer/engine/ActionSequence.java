package org.firstinspires.ftc.teamcode.sequencer.engine;

import android.util.Log;

import org.firstinspires.ftc.teamcode.sequencer.actions.RobotAction;

import java.util.ArrayList;

public class ActionSequence {
    public ArrayList<RobotAction> actions = new ArrayList<>();
    private int currentAction = 0;

    public void update(){
        RobotAction action = actions.get(currentAction);
        action.update();
        if(action.isDone()){
            currentAction++;
        }
    }

    public void append(ActionSequence sequence){
        for(RobotAction a: sequence.actions){
            this.actions.add(a);
        }
    }

    public boolean isDone(){
        return !(currentAction < actions.size());
    }

    public void add(RobotAction action){
        if(action == null){
            Log.e("ActionSequence", "add function is being used on a null action");
        }
        actions.add(action);
    }
}
