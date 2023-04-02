package edu.gatech.churchill.cs7641.gridworld;

import burlap.behavior.singleagent.auxiliary.performance.LearningAlgorithmExperimenter;
import burlap.behavior.singleagent.auxiliary.performance.PerformanceMetric;
import burlap.behavior.singleagent.auxiliary.performance.TrialMode;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;


public class GridWorldExperiment {

    private static final String VALUE_ITERATION = "VI";
    private static final String POLICY_ITERATION = "PI";
    private static final String Q_LEARNING = "QL";
    private static final String OUTPUT_DIRECTORY_PATH = "gridworld";
    private static int width, height;

    public static void main(String[] args) {
        if(args.length != 3) {
            printUsageMessage();
        }
        String algorithm = args[0];
        try {
            width = Integer.parseInt(args[1]);
            height = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            printUsageMessage();
        }

        float probabilityOfSuccessfulTransition = 0.8f;
        GridWorldProblem problem = new GridWorldProblem(width, height, probabilityOfSuccessfulTransition);

        switch (algorithm) {
            case Q_LEARNING -> qLearningExperiment(problem);
            case VALUE_ITERATION -> valueIterationExperiment(problem);
            case POLICY_ITERATION -> policyIterationExperiment(problem);
            default -> printUsageMessage();
        }
    }

    private static void valueIterationExperiment(GridWorldProblem problem) {
        GridWorldAnalysis analysis = problem.createValueIterationAnalysis();
        analysis.episode.write(String.format("%s/vi_%dx%d",OUTPUT_DIRECTORY_PATH, width, height));

        analysis.gui.initGUI();
        problem.createVisualizer(OUTPUT_DIRECTORY_PATH);

        System.out.println(analysis.optimalReward);
    }

    private static void policyIterationExperiment(GridWorldProblem problem) {
        GridWorldAnalysis analysis = problem.createPolicyIterationAnalysis();
        analysis.episode.write(String.format("%s/pi_%dx%d",OUTPUT_DIRECTORY_PATH, width, height));

        analysis.gui.initGUI();
        problem.createVisualizer(OUTPUT_DIRECTORY_PATH);

        System.out.println(analysis.optimalReward);
    }

    private static void qLearningExperiment(GridWorldProblem problem) {
        LearningAgentFactory[] agentFactories = new LearningAgentFactory[]{
                problem.createQLearningAgentFactory()
        };

        var experiment = constructExperiment(problem, agentFactories);
        experiment.startExperiment();
    }

    private static LearningAlgorithmExperimenter constructExperiment(GridWorldProblem problem, LearningAgentFactory[] agentFactories) {
        SimulatedEnvironment environment = problem.getSimulatedEnvironment();

        LearningAlgorithmExperimenter experimenter = new LearningAlgorithmExperimenter(environment, 10, 100, agentFactories);
        experimenter.setUpPlottingConfiguration(500, 250, 2, 1000,
                TrialMode.MOST_RECENT_AND_AVERAGE,
                PerformanceMetric.CUMULATIVE_STEPS_PER_EPISODE,
                PerformanceMetric.AVERAGE_EPISODE_REWARD);

        return experimenter;
    }

    private static void printUsageMessage() {
        System.err.println("Usage: [algorithm] [grid width] [grid height]");
        System.err.println("Supported algorithms:");
        System.err.println(String.format("\t'%s' - Value Iteration", VALUE_ITERATION));
        System.err.println(String.format("\t'%s' - Policy Iteration", POLICY_ITERATION));
        System.err.println(String.format("\t'%s' - Q-Learning", Q_LEARNING));
        System.exit(1);
    }

}
