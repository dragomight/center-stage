package org.firstinspires.ftc.teamcode.sequencer.engine;

/**
 * The sequence director will play a sequence that is given to it, then throw it away and do nothing
 * until given a new sequence.
 */
public class SequenceDirector {

    private ActionSequence sequence;

    public void addSequence(ActionSequence actionSequence){
        if(sequence == null) {
            this.sequence = actionSequence;
        }
        else{
            sequence.append(actionSequence);
        }
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
