package edu.gatech.churchill.cs7641.gridworld;

import burlap.behavior.singleagent.auxiliary.performance.LearningAlgorithmExperimenter;
import burlap.behavior.singleagent.auxiliary.performance.PerformanceMetric;
import burlap.behavior.singleagent.auxiliary.performance.TrialMode;
import burlap.mdp.auxiliary.common.ConstantStateGenerator;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;

import java.util.List;

public class GridWorldExperiment {

    public static void main(String args[]) {
        int width = 11, height = 11;
        float probabilityOfSuccessfulTransition = 0.8f;
        GridWorldProblem problem = new GridWorldProblem(width, height, probabilityOfSuccessfulTransition);

        var experiment = constructExperiment(problem);
        experiment.startExperiment();
    }

    private static LearningAlgorithmExperimenter constructExperiment(GridWorldProblem problem) {
        ConstantStateGenerator stateGenerator = new ConstantStateGenerator(problem.getInitialState());
        SimulatedEnvironment environment = new SimulatedEnvironment(problem.getDomain(), stateGenerator);

        LearningAlgorithmExperimenter experimenter = new LearningAlgorithmExperimenter(environment, 10, 100, problem.createAgentFactories());
        experimenter.setUpPlottingConfiguration(500, 250, 2, 1000,
                TrialMode.MOST_RECENT_AND_AVERAGE,
                PerformanceMetric.CUMULATIVE_STEPS_PER_EPISODE,
                PerformanceMetric.AVERAGE_EPISODE_REWARD);

        return experimenter;
    }

}
