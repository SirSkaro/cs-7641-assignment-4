package edu.gatech.churchill.cs7641.mountaincar;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.shell.visual.VisualExplorer;

public class MountainCarAnalysis {

    public Policy policy;
    public Episode episode;
    public VisualExplorer gui;
    public double optimalReward;
    public ValueFunction planner;

}
