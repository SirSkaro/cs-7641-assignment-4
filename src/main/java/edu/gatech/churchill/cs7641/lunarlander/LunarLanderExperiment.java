package edu.gatech.churchill.cs7641.lunarlander;

import burlap.domain.singleagent.lunarlander.LunarLanderDomain;
import edu.gatech.churchill.cs7641.gridworld.GridWorldAnalysis;
import edu.gatech.churchill.cs7641.gridworld.GridWorldProblem;

public class LunarLanderExperiment {

    private static final String VALUE_ITERATION = "VI";
    private static final String POLICY_ITERATION = "PI";
    private static final String Q_LEARNING = "QL";
    private static final String OUTPUT_DIRECTORY_PATH = "lunarlander";

    public static void main(String[] args) {
        if(args.length != 1) {
            printUsageMessage();
        }
        String algorithm = args[0];
        LunarLanderProblem problem = new LunarLanderProblem();

        switch (algorithm) {
            case Q_LEARNING -> qLearningExperiment(problem);
            case VALUE_ITERATION -> valueIterationExperiment(problem);
//            case POLICY_ITERATION -> policyIterationExperiment(problem);
            default -> printUsageMessage();
        }
    }

    private static void valueIterationExperiment(LunarLanderProblem problem) {
        LunarLanderAnalysis analysis = problem.createValueIterationAnalysis();
        analysis.episode.write(String.format("%s/vi",OUTPUT_DIRECTORY_PATH));

        analysis.gui.setVisible(true);
        //problem.createVisualizer(OUTPUT_DIRECTORY_PATH);
    }

    private static void qLearningExperiment(LunarLanderProblem problem) {
        LunarLanderAnalysis analysis = problem.createQLearningAnalysis();
        analysis.episode.write(String.format("%s/ql", OUTPUT_DIRECTORY_PATH));

        analysis.gui.initGUI();
    }

    private static void printUsageMessage() {
        System.err.println("Usage: [algorithm]");
        System.err.println("Supported algorithms:");
        System.err.println(String.format("\t'%s' - Value Iteration", VALUE_ITERATION));
        System.err.println(String.format("\t'%s' - Policy Iteration", POLICY_ITERATION));
        System.err.println(String.format("\t'%s' - Q-Learning", Q_LEARNING));
        System.exit(1);
    }

}
