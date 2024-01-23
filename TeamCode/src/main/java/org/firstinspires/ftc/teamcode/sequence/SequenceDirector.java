package org.firstinspires.ftc.teamcode.sequence;

import org.firstinspires.ftc.teamcode.BillsUnexpectedRoadtrip.Cadbot;

/**
 * The sequence director will play a sequence that is given to it, then throw it away and do nothing
 * until given a new sequence.
 */
public class SequenceDirector {

    private Cadbot cadbot;
    private ActionSequence sequence;

    public void addSequence(ActionSequence actionSequence){
        this.sequence = actionSequence;
    }

    public void update(){
        if(sequence == null){
            return;
        }
        sequence.update();
        if(sequence.isDone()){
            sequence = null;
            return;
        }
    }

}
