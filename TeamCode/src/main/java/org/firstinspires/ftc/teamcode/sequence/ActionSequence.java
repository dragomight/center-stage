package org.firstinspires.ftc.teamcode.sequence;

import java.util.ArrayList;

public class ActionSequence {
    private ArrayList<RobotAction> actions = new ArrayList<RobotAction>();
    private int currentAction = 0;

    public void update(){
        RobotAction action = actions.get(currentAction);
        action.update();
        if(action.isDone()){
            currentAction++;
        }
    }

    public boolean isDone(){
        return !(currentAction < actions.size());
    }

    public void add(RobotAction action){
        actions.add(action);
    }
}
