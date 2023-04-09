package edu.gatech.churchill.cs7641.lunarlander;

import burlap.behavior.policy.EpsilonGreedy;
import burlap.behavior.policy.GreedyDeterministicQPolicy;
import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.domain.singleagent.lunarlander.LLVisualizer;
import burlap.domain.singleagent.lunarlander.LunarLanderDomain;
import burlap.domain.singleagent.lunarlander.state.LLState;
import burlap.mdp.auxiliary.common.ConstantStateGenerator;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.shell.visual.VisualExplorer;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import edu.gatech.churchill.cs7641.*;
import edu.gatech.churchill.cs7641.frozenlake.FrozenLakeAnalysis;

public class LunarLanderProblem {

    private LunarLanderDomain world;
    private OOSADomain singleAgentDomain;
    private SimpleHashableStateFactory hashingFactory;
    private LLState initialState;
    private SimulatedEnvironment simulatedEnvironment;
    private ProblemSize problemSize;

    public LunarLanderProblem(ProblemSize problemSize) {
        this.problemSize = problemSize;
        hashingFactory = new SimpleHashableStateFactory();
        initialState = problemSize.initialState;
        world = problemSize.world;
        singleAgentDomain = problemSize.singleAgentDomain;

        ConstantStateGenerator stateGenerator = new ConstantStateGenerator(initialState);
        simulatedEnvironment = new SimulatedEnvironment(singleAgentDomain, stateGenerator);
    }

    public LunarLanderAnalysis createValueIterationAnalysis() {
        double maxDelta = .001;
        int maxIterations = 100;
        double gamma = 0.99;

        RecordingValueIteration planner = new RecordingValueIteration(singleAgentDomain, gamma, hashingFactory, maxDelta, maxIterations);
        Policy policy = planner.planFromState(initialState);

        return createAnalysis(planner, policy, planner.getAnalysis());
    }

    public LunarLanderAnalysis createPolicyIterationAnalysis() {
        double maxDelta = 0.001;
        int maxEvaluationIterations = 200;
        int maxPolicyIterations = 100;
        double gamma = 0.99;

        RecordingPolicyIteration planner = new RecordingPolicyIteration(singleAgentDomain, gamma, hashingFactory, maxDelta, maxEvaluationIterations, maxPolicyIterations);
        Policy policy = planner.planFromState(initialState);

        return createAnalysis(planner, policy, planner.getAnalysis());
    }

    public LunarLanderAnalysis createQLearningAnalysis() {
        double gamma = problemSize.gamma;
        double qInit = problemSize.qInit;
        double learningRate = problemSize.learningRate;
        double thresholdDelta = problemSize.thresholdDelta;
        GreedyDeterministicQPolicy explorationPolicy = new GreedyDeterministicQPolicy();
        int maxNumberOfEpisodes = 20_000;

        RecordingQLearning agent = new RecordingQLearning(singleAgentDomain, gamma, hashingFactory, qInit, learningRate, explorationPolicy, maxNumberOfEpisodes, thresholdDelta);
        explorationPolicy.setSolver(agent);
        Policy policy = agent.planFromState(initialState);
        //explorationPolicy.resetEpsilon();

        return createAnalysis(agent, policy, agent.getAnalysis());
    }

    private LunarLanderAnalysis createAnalysis(ValueFunction planner, Policy policy, GeneralAnalysis generalAnalysis) {
        LunarLanderAnalysis analysis = new LunarLanderAnalysis();
        analysis.planner = planner;
        analysis.policy = policy;
        analysis.episode = PolicyUtils.rollout(policy, simulatedEnvironment);
        analysis.generalAnalysis = generalAnalysis;

        analysis.gui = new VisualExplorer(singleAgentDomain, LLVisualizer.getVisualizer(world), initialState);

        return analysis;
    }

}
