package edu.gatech.churchill.cs7641.gridworld;

import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.planning.stochastic.DynamicProgramming;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.domain.singleagent.gridworld.GridWorldDomain;
import burlap.domain.singleagent.gridworld.GridWorldRewardFunction;
import burlap.domain.singleagent.gridworld.GridWorldTerminalFunction;
import burlap.domain.singleagent.gridworld.GridWorldVisualizer;
import burlap.domain.singleagent.gridworld.state.GridAgent;
import burlap.domain.singleagent.gridworld.state.GridLocation;
import burlap.domain.singleagent.gridworld.state.GridWorldState;
import burlap.mdp.auxiliary.common.ConstantStateGenerator;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import burlap.visualizer.Visualizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GridWorldProblem {

    private static final float NON_GOAL_REWARD = -0.1f;
    private static final float THIN_ICE_REWARD = -1f;
    private static final int GOAL_LOCATION_TYPE = 0;
    private static final int THIN_ICE_LOCATION_TYPE = 1;

    private OOSADomain singleAgentDomain;
    private GridWorldDomain gridWorld;
    private GridWorldState initialState;
    private SimpleHashableStateFactory hashingFactory;
    private SimulatedEnvironment simulatedEnvironment;

    public GridWorldProblem(int width, int height, float probabilityOfSuccessfulTransition) {
        gridWorld = new GridWorldDomain(width, height);
        gridWorld.setMapToFourRooms();
        gridWorld.setProbSucceedTransitionDynamics(probabilityOfSuccessfulTransition);
        gridWorld.setNumberOfLocationTypes(2);

        GridWorldTerminalFunction terminalFunction = new GridWorldTerminalFunction();
        terminalFunction.markAsTerminalPosition(width - 1, height - 1); // Top right is the goal state
        GridWorldRewardFunction rewardFunction = new GridWorldRewardFunction(width, height, NON_GOAL_REWARD);

        var locations = setupHazards(width, height, rewardFunction);
        locations.add(new GridLocation(width - 1, height - 1, GOAL_LOCATION_TYPE, "goal"));

        gridWorld.setTf(terminalFunction);
        gridWorld.setRf(rewardFunction);

        // Don't know if the order of these functions matters
        singleAgentDomain = gridWorld.generateDomain();
        initialState = new GridWorldState(new GridAgent(0, 0), locations);
        hashingFactory = new SimpleHashableStateFactory();

        ConstantStateGenerator stateGenerator = new ConstantStateGenerator(initialState);
        simulatedEnvironment = new SimulatedEnvironment(singleAgentDomain, stateGenerator);
    }

    private List<GridLocation> setupHazards(int width, int height, GridWorldRewardFunction rewardFunction) {
        int[][] map = gridWorld.getMap();
        int numHazards = (width * height) / 5; //1 hazard per 5 squares
        Random random = new Random(map.length);
        var locations = new ArrayList<GridLocation>();

        for(int i=0; i < numHazards; i++) {
            int xPos = random.nextInt(width);
            int yPos = random.nextInt(height);

            if(map[xPos][yPos] == 1) { //if this is a wall, generate again
                i--;
                continue;
            }
            rewardFunction.setReward(xPos, yPos, THIN_ICE_REWARD);
            locations.add(new GridLocation(xPos, yPos, THIN_ICE_LOCATION_TYPE, String.format("thin ice %d", i+1)));
        }

        return locations;
    }

    public EpisodeSequenceVisualizer createVisualizer(String outputDirectoryPath) {
        Visualizer visualizer = GridWorldVisualizer.getVisualizer(gridWorld.getMap());
        return new EpisodeSequenceVisualizer(visualizer, singleAgentDomain, outputDirectoryPath);
    }

    public SimulatedEnvironment getSimulatedEnvironment() {
        return simulatedEnvironment;
    }

    public GridWorldAnalysis createValueIterationAnalysis() {
        double maxDelta = 0.001;
        int maxIterations = 100;
        double gamma = 0.99;

        ValueIteration planner = new ValueIteration(singleAgentDomain, gamma, hashingFactory, maxDelta, maxIterations);
        Policy policy = planner.planFromState(initialState);

        return createAnalysis(planner, policy);
    }

    public GridWorldAnalysis createPolicyIterationAnalysis() {
        double maxDelta = 0.001;
        int maxEvaluationIterations = 200;
        int maxPolicyIterations = 100;
        double gamma = 0.99;

        PolicyIteration planner = new PolicyIteration(singleAgentDomain, gamma, hashingFactory, maxDelta, maxEvaluationIterations, maxPolicyIterations);
        Policy policy = planner.planFromState(initialState);

        return createAnalysis(planner, policy);
    }

    private GridWorldAnalysis createAnalysis(DynamicProgramming planner, Policy policy) {
        GridWorldAnalysis analysis = new GridWorldAnalysis();
        analysis.policy = policy;
        analysis.episode = PolicyUtils.rollout(policy, simulatedEnvironment);

        analysis.gui = GridWorldDomain.getGridWorldValueFunctionVisualization(
                planner.getAllStates(),
                gridWorld.getWidth(), gridWorld.getHeight(),
                planner, policy);

        return analysis;
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
