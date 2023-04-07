package edu.gatech.churchill.cs7641.frozenlake;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.ValueFunctionVisualizerGUI;
import burlap.behavior.valuefunction.ValueFunction;
import edu.gatech.churchill.cs7641.GeneralAnalysis;

public class FrozenLakeAnalysis {

    public Policy policy;
    public Episode episode;
    public ValueFunctionVisualizerGUI gui;
    public GeneralAnalysis generalAnalysis;
    public ValueFunction planner;

}
