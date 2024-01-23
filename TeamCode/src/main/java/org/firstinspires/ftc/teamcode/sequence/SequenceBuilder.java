package org.firstinspires.ftc.teamcode.sequence;

public class SequenceBuilder {

    ActionSequence sequence;

    public SequenceBuilder addAction(RobotAction action){
        if(sequence == null){
            sequence = new ActionSequence();
        }
        sequence.add(action);
        return this;
    }
}
