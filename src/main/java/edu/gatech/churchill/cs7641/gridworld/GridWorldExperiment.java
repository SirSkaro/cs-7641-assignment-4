package edu.gatech.churchill.cs7641.gridworld;

import burlap.behavior.singleagent.auxiliary.performance.LearningAlgorithmExperimenter;
import burlap.behavior.singleagent.auxiliary.performance.PerformanceMetric;
import burlap.behavior.singleagent.auxiliary.performance.TrialMode;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;


public class GridWorldExperiment {

    public static void main(String args[]) {
        int width = 11, height = 11;
        float probabilityOfSuccessfulTransition = 0.8f;
        GridWorldProblem problem = new GridWorldProblem(width, height, probabilityOfSuccessfulTransition);
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

}
