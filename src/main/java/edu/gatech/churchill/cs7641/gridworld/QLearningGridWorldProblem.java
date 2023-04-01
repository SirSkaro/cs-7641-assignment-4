package edu.gatech.churchill.cs7641.gridworld;

import burlap.behavior.singleagent.auxiliary.performance.LearningAlgorithmExperimenter;
import burlap.behavior.singleagent.auxiliary.performance.PerformanceMetric;
import burlap.behavior.singleagent.auxiliary.performance.TrialMode;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.mdp.auxiliary.common.ConstantStateGenerator;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.statehashing.simple.SimpleHashableStateFactory;

public class QLearningGridWorldProblem extends BaseGridWorldProblem {

    protected QLearningGridWorldProblem(int width, int height, float probabilityOfSuccessfulTransition) {
        super(width, height, probabilityOfSuccessfulTransition);
    }

    @Override
    LearningAlgorithmExperimenter createExperiment() {
        ConstantStateGenerator sg = new ConstantStateGenerator(this.initialState);

        LearningAgentFactory qLearningFactory = createFactory();
        SimulatedEnvironment env = new SimulatedEnvironment(domain, sg);

        LearningAlgorithmExperimenter exp = new LearningAlgorithmExperimenter(env, 10, 100, qLearningFactory);

        exp.setUpPlottingConfiguration(500, 250, 2, 1000,
                TrialMode.MOST_RECENT_AND_AVERAGE,
                PerformanceMetric.CUMULATIVE_STEPS_PER_EPISODE,
                PerformanceMetric.AVERAGE_EPISODE_REWARD);

        return exp;
    }

    private LearningAgentFactory createFactory() {
        return new LearningAgentFactory() {

            public String getAgentName() {
                return "Q-learning";
            }

            public LearningAgent generateAgent() {
                SimpleHashableStateFactory hashingFactory = new SimpleHashableStateFactory();
                return new QLearning(domain, 0.99, hashingFactory, 0.3, 0.1);
            }
        };
    }
}
