package org.firstinspires.ftc.teamcode.fuzzy;

/**
 * The Pav Tracker takes power settings to a motor or system combined with the current velocity,
 * to anticipate the resulting acceleration.  The assumption here is that position doesn't have an
 * impact on the resulting acceleration.  If it does, e.g. an arm, then you need a Pavx tracker.
 *
 * The tracker's second function is to produce a power value that will result in the desired
 * acceleration given the current velocity.
 * The method employed by this class is to granularize the data into bins.
 * If the output needs to have a non-stepped response, interpolation should work.
 */
public class PavTracker {

    public void update(double power, double acc, double vel, double pos){
        //
    }
}
