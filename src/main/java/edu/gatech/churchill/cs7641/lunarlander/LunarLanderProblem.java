package edu.gatech.churchill.cs7641.lunarlander;

import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.domain.singleagent.lunarlander.LLVisualizer;
import burlap.domain.singleagent.lunarlander.LunarLanderDomain;
import burlap.domain.singleagent.lunarlander.state.LLAgent;
import burlap.domain.singleagent.lunarlander.state.LLBlock;
import burlap.domain.singleagent.lunarlander.state.LLState;
import burlap.mdp.auxiliary.common.ConstantStateGenerator;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.shell.visual.VisualExplorer;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import edu.gatech.churchill.cs7641.DecayingEpsilonGreedy;

import java.util.List;

public class LunarLanderProblem {

    private LunarLanderDomain world;
    private OOSADomain singleAgentDomain;
    private SimpleHashableStateFactory hashingFactory;
    private LLState initialState;
    private SimulatedEnvironment simulatedEnvironment;

    public LunarLanderProblem() {
        hashingFactory = new SimpleHashableStateFactory();

        initialState = new LLState(
                new LLAgent(4, 5, 0),
                new LLBlock.LLPad(5, 6, 0, 2, "goal"),
                new LLBlock.LLObstacle(3, 3, 0, 7, "left wall"),
                new LLBlock.LLObstacle(7, 7, 0, 7, "right wall")
        );
        world = new LunarLanderDomain();
        world.setXmin(3);
        world.setYmin(0);
        world.setYmax(7);
        world.setXmax(7);
        world.setGravity(-0.2);
        world.setAngmax(Math.PI/4);
        world.setAnginc(Math.PI/4);
        world.setTf(new LandedTerminalFunction(world));
        world.setRf(new GoalProximityRewardFunction(world, initialState.pad));

        singleAgentDomain = world.generateDomain();
        singleAgentDomain.setActionTypes(new UniversalActionType(LunarLanderDomain.ACTION_TURN_LEFT))
                .addActionType(new UniversalActionType(LunarLanderDomain.ACTION_TURN_RIGHT))
                .addActionType(new LunarLanderDomain.ThrustType(List.of(0.15)));

        ConstantStateGenerator stateGenerator = new ConstantStateGenerator(initialState);
        simulatedEnvironment = new SimulatedEnvironment(singleAgentDomain, stateGenerator);
    }

    public LunarLanderAnalysis createValueIterationAnalysis() {
        double maxDelta = 0.001;
        int maxIterations = 100;
        double gamma = 0.99;

        ValueIteration planner = new ValueIteration(singleAgentDomain, gamma, hashingFactory, maxDelta, maxIterations);
        Policy policy = planner.planFromState(initialState);

        return createAnalysis(planner, policy);
    }

    public LunarLanderAnalysis createQLearningAnalysis() {
        double gamma = 0.99;
        double qInit = 1.0;
        double learningRate = 0.1;
        DecayingEpsilonGreedy explorationPolicy = new DecayingEpsilonGreedy(1.0, 0.999);
        int maxNumberOfEpisodes = Integer.MAX_VALUE;

        QLearning agent = new QLearning(singleAgentDomain, gamma, hashingFactory, qInit, learningRate, explorationPolicy, maxNumberOfEpisodes);
        explorationPolicy.setSolver(agent);
        Policy policy = null;

        for(int trial = 0; trial < 5000; trial++) {
            policy = agent.planFromState(initialState);
            explorationPolicy.resetEpsilon();
        }

        return createAnalysis(agent, policy);
    }

    private LunarLanderAnalysis createAnalysis(ValueFunction planner, Policy policy) {
        LunarLanderAnalysis analysis = new LunarLanderAnalysis();
        analysis.planner = planner;
        analysis.policy = policy;
        analysis.episode = PolicyUtils.rollout(policy, simulatedEnvironment);

        analysis.gui = new VisualExplorer(singleAgentDomain, LLVisualizer.getVisualizer(world), initialState);

        return analysis;
    }

}
