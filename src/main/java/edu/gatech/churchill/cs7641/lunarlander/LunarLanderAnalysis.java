package edu.gatech.churchill.cs7641.lunarlander;

import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.ValueFunctionVisualizerGUI;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.shell.visual.VisualExplorer;
import burlap.visualizer.Visualizer;

public class LunarLanderAnalysis {

    public Policy policy;
    public Episode episode;
    public VisualExplorer gui;
    public double optimalReward;
    public ValueFunction planner;

}
