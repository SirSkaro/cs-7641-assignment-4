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
    private static final float HAZARD_REWARD = -1f;
    private static final int GOAL_LOCATION_TYPE = 0;
    private static final int HAZARD_LOCATION_TYPE = 1;

    private OOSADomain singleAgentDomain;
    private GridWorldDomain gridWorld;
    private GridWorldState initialState;
    private SimpleHashableStateFactory hashingFactory;
    private SimulatedEnvironment simulatedEnvironment;

    public GridWorldProblem(int width, int height, float probabilityOfSuccessfulTransition) {
        gridWorld = new GridWorldDomain(width, height);
        gridWorld.setProbSucceedTransitionDynamics(probabilityOfSuccessfulTransition);
        gridWorld.setNumberOfLocationTypes(2);

        GridWorldTerminalFunction terminalFunction = new GridWorldTerminalFunction();
        terminalFunction.markAsTerminalPosition(width - 1, height - 1); // Top right is the goal state
        GridWorldRewardFunction rewardFunction = new GridWorldRewardFunction(width, height, NON_GOAL_REWARD);

        setupWalls(width, height);
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

    private void setupWalls(int width, int height) {
        //Create 4 rooms
        gridWorld.horizontalWall(0, width-1, height/2);
        gridWorld.verticalWall(0, height-1, width/2);

        //Create 2 openings per wall. Collision is ok
        Random random = new Random((long) width * height);
        for(int i=0; i < 2; i++) {
            int westWallSegmentToOpen = random.nextInt(width/2);
            gridWorld.clearLocationOfWalls(westWallSegmentToOpen, height/2);

            int eastWallSegmentToOpen = random.nextInt(width/2 + 1, width);
            gridWorld.clearLocationOfWalls(eastWallSegmentToOpen, height/2);

            int southWallSegmentToOpen = random.nextInt(height/2);
            gridWorld.clearLocationOfWalls(width/2, southWallSegmentToOpen);

            int northWallSegmentToOpen = random.nextInt(height/2 + 1, height);
            gridWorld.clearLocationOfWalls(width/2, northWallSegmentToOpen);
        }
    }

    private List<GridLocation> setupHazards(int width, int height, GridWorldRewardFunction rewardFunction) {
        int[][] map = gridWorld.getMap();
        int numHazards = (width * height) / 5; //1 hazard per 5 squares
        Random random = new Random(numHazards);
        var locations = new ArrayList<GridLocation>();

        for(int i=0; i < numHazards; i++) {
            int xPos = random.nextInt(width);
            int yPos = random.nextInt(height);

            if(map[xPos][yPos] == 1) { //if this is a wall, generate again
                i--;
                continue;
            }
            rewardFunction.setReward(xPos, yPos, HAZARD_REWARD);
            locations.add(new GridLocation(xPos, yPos, HAZARD_LOCATION_TYPE, String.format("thin ice %d", i+1)));
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
