package edu.gatech.churchill.cs7641.gridworld;

import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.planning.Planner;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.domain.singleagent.gridworld.GridWorldDomain;
import burlap.domain.singleagent.gridworld.GridWorldVisualizer;
import burlap.domain.singleagent.gridworld.state.GridAgent;
import burlap.domain.singleagent.gridworld.state.GridLocation;
import burlap.domain.singleagent.gridworld.state.GridWorldState;
import burlap.mdp.auxiliary.common.ConstantStateGenerator;
import burlap.mdp.auxiliary.common.SinglePFTF;
import burlap.mdp.auxiliary.stateconditiontest.TFGoalCondition;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.common.GoalBasedRF;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import burlap.visualizer.Visualizer;

public class GridWorldProblem {

    private static final float GOAL_REWARD = 5;
    private static final float NON_GOAL_REWARD = -0.1f;

    private OOSADomain singleAgentDomain;
    private GridWorldDomain gridWorld;
    private GridWorldState initialState;
    private SimpleHashableStateFactory hashingFactory;
    private SimulatedEnvironment simulatedEnvironment;

    public GridWorldProblem(int width, int height, float probabilityOfSuccessfulTransition) {
        gridWorld = new GridWorldDomain(width, height);
        gridWorld.setMapToFourRooms();
        gridWorld.setProbSucceedTransitionDynamics(probabilityOfSuccessfulTransition);

        TerminalFunction terminalFunction = new SinglePFTF(PropositionalFunction.findPF(gridWorld.generatePfs(), GridWorldDomain.PF_AT_LOCATION));
        RewardFunction rewardFunction = new GoalBasedRF(new TFGoalCondition(terminalFunction), GOAL_REWARD, NON_GOAL_REWARD);

        gridWorld.setTf(terminalFunction);
        gridWorld.setRf(rewardFunction);

        // Don't know if the order of these functions matters
        singleAgentDomain = gridWorld.generateDomain();
        // Put goal state in top right corner and starting state in bottom left
        initialState = new GridWorldState(new GridAgent(0, 0), new GridLocation(width - 1, height - 1, "loc0"));
        hashingFactory = new SimpleHashableStateFactory();

        ConstantStateGenerator stateGenerator = new ConstantStateGenerator(initialState);
        simulatedEnvironment = new SimulatedEnvironment(singleAgentDomain, stateGenerator);
    }

    public SADomain getSingleAgentDomain() {
        return singleAgentDomain;
    }

    public EpisodeSequenceVisualizer createVisualizer(String outputDirectoryPath) {
        Visualizer visualizer = GridWorldVisualizer.getVisualizer(gridWorld.getMap());
        return new EpisodeSequenceVisualizer(visualizer, singleAgentDomain, outputDirectoryPath);
    }

    public SimulatedEnvironment getSimulatedEnvironment() {
        return simulatedEnvironment;
    }

    public GridWorldAnalysis createValueIterationEpisode() {
        double maxDelta = 0.001;
        int maxIterations = 100;
        double gamma = 0.99;

        ValueIteration planner = new ValueIteration(singleAgentDomain, gamma, hashingFactory, maxDelta, maxIterations);
        Policy policy = planner.planFromState(initialState);

        GridWorldAnalysis analysis = new GridWorldAnalysis();
        analysis.policy = policy;
        analysis.episode = PolicyUtils.rollout(policy, simulatedEnvironment);
        analysis.gui = GridWorldDomain.getGridWorldValueFunctionVisualization(
                planner.getAllStates(),
                gridWorld.getWidth(), gridWorld.getHeight(),
                planner, policy);

        return analysis;
    }

    public Episode createPolicyIterationEpisode() {
        double maxDelta = 0.001;
        int maxEvaluationIterations = 200;
        int maxPolicyIterations = 100;
        double gamma = 0.99;

        Planner planner = new PolicyIteration(singleAgentDomain, gamma, hashingFactory, maxDelta, maxEvaluationIterations, maxPolicyIterations);
        Policy policy = planner.planFromState(initialState);
        return PolicyUtils.rollout(policy, simulatedEnvironment);
    }

    public LearningAgentFactory createQLearningAgentFactory() {
        return new LearningAgentFactory() {

            public String getAgentName() {
                return "Q-learning";
            }

            public LearningAgent generateAgent() {
                return new QLearning(singleAgentDomain, 0.99, hashingFactory, 0.3, 0.1);
            }
        };
    }

}
