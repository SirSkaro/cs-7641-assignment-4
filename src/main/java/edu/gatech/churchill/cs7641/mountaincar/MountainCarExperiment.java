package edu.gatech.churchill.cs7641.mountaincar;

import edu.gatech.churchill.cs7641.lunarlander.LunarLanderAnalysis;
import edu.gatech.churchill.cs7641.lunarlander.LunarLanderProblem;

public class MountainCarExperiment {

    private static final String VALUE_ITERATION = "VI";
    private static final String POLICY_ITERATION = "PI";
    private static final String Q_LEARNING = "QL";
    private static final String OUTPUT_DIRECTORY_PATH = "mountaincar";

    public static void main(String[] args) {
        if(args.length != 1) {
            printUsageMessage();
        }
        String algorithm = args[0];
        MountainCarProblem problem = new MountainCarProblem();

        switch (algorithm) {
//            case Q_LEARNING -> qLearningExperiment(problem);
            case VALUE_ITERATION -> valueIterationExperiment(problem);
//            case POLICY_ITERATION -> policyIterationExperiment(problem);
            default -> printUsageMessage();
        }


    }

    private static void valueIterationExperiment(MountainCarProblem problem) {
        MountainCarAnalysis analysis = problem.createValueIterationAnalysis();
        analysis.episode.write(String.format("%s/vi",OUTPUT_DIRECTORY_PATH));

        analysis.gui.initGUI();
        //problem.createVisualizer(OUTPUT_DIRECTORY_PATH);
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
