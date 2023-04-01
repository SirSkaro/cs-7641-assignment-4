package edu.gatech.churchill.cs7641.gridworld;

import burlap.behavior.singleagent.auxiliary.performance.LearningAlgorithmExperimenter;
import burlap.behavior.singleagent.auxiliary.performance.PerformanceMetric;
import burlap.behavior.singleagent.auxiliary.performance.TrialMode;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.domain.singleagent.gridworld.GridWorldDomain;
import burlap.domain.singleagent.gridworld.state.GridAgent;
import burlap.domain.singleagent.gridworld.state.GridLocation;
import burlap.domain.singleagent.gridworld.state.GridWorldState;
import burlap.mdp.auxiliary.common.ConstantStateGenerator;
import burlap.mdp.auxiliary.common.SinglePFTF;
import burlap.mdp.auxiliary.stateconditiontest.TFGoalCondition;
import burlap.mdp.core.Domain;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.common.GoalBasedRF;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;

public class GridWorldProblem {

    private static final float GOAL_REWARD = 5;
    private static final float NON_GOAL_REWARD = -0.1f;

    private OOSADomain domain;
    private GridWorldState initialState;
    private SimpleHashableStateFactory hashingFactory;

    public GridWorldProblem(int width, int height, float probabilityOfSuccessfulTransition) {
        GridWorldDomain gridWorld = new GridWorldDomain(width, height);
        gridWorld.setMapToFourRooms();
        gridWorld.setProbSucceedTransitionDynamics(probabilityOfSuccessfulTransition);

        TerminalFunction terminalFunction = new SinglePFTF(PropositionalFunction.findPF(gridWorld.generatePfs(), GridWorldDomain.PF_AT_LOCATION));
        RewardFunction rewardFunction = new GoalBasedRF(new TFGoalCondition(terminalFunction), GOAL_REWARD, NON_GOAL_REWARD);

        gridWorld.setTf(terminalFunction);
        gridWorld.setRf(rewardFunction);

        // Don't know if the order of these functions matters
        domain = gridWorld.generateDomain();
        // Put goal state in top right corner and starting state in bottom left
        initialState = new GridWorldState(new GridAgent(0, 0), new GridLocation(width - 1, height - 1, "loc0"));
        hashingFactory = new SimpleHashableStateFactory();
    }

    public SADomain getDomain() {
        return domain;
    }

    public GridWorldState getInitialState() {
        return initialState;
    }

    public LearningAgentFactory[] createAgentFactories() {
        return new LearningAgentFactory[]{createQLearningAgentFactory()};
    }

    private LearningAgentFactory createQLearningAgentFactory() {
        return new LearningAgentFactory() {

            public String getAgentName() {
                return "Q-learning";
            }

            public LearningAgent generateAgent() {
                return new QLearning(domain, 0.99, hashingFactory, 0.3, 0.1);
            }
        };
    }

}
