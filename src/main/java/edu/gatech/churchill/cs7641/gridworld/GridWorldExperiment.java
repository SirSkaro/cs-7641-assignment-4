package edu.gatech.churchill.cs7641.gridworld;

import burlap.behavior.singleagent.Episode;
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

    public static void main(String args[]) {
        if(args.length != 1) {
            printUsageMessage();
        }
        String algorithm = args[0];
        int width = 11, height = 11;
        float probabilityOfSuccessfulTransition = 0.8f;
        GridWorldProblem problem = new GridWorldProblem(width, height, probabilityOfSuccessfulTransition);

        if(algorithm.equals(Q_LEARNING)) {
            qLearningExperiment(problem);
        } else if (algorithm.equals(VALUE_ITERATION)) {
            valueIterationExperiment(problem);
        } else if (algorithm.equals(POLICY_ITERATION)) {
            System.out.println("PI");
        } else {
            printUsageMessage();
        }
    }

    private static void valueIterationExperiment(GridWorldProblem problem) {
        Episode episode = problem.createValueIterationEpisode();
        episode.write(OUTPUT_DIRECTORY_PATH + "/vi");

        problem.createVisualizer(OUTPUT_DIRECTORY_PATH);
    }

    private static void policyIterationExperiment(GridWorldProblem problem) {
        Episode episode = problem.createPolicyIterationEpisode();
        episode.write(OUTPUT_DIRECTORY_PATH + "/pi");

        problem.createVisualizer(OUTPUT_DIRECTORY_PATH);
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
        System.err.println("Please specify (only) one of the following algorithms as an argument:");
        System.err.println(String.format("\t'%s' - Value Iteration", VALUE_ITERATION));
        System.err.println(String.format("\t'%s' - Policy Iteration", POLICY_ITERATION));
        System.err.println(String.format("\t'%s' - Q-Learning", Q_LEARNING));
        System.exit(1);
    }

}
