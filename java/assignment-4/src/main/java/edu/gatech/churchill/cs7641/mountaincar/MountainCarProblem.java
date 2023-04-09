package edu.gatech.churchill.cs7641.mountaincar;

import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.domain.singleagent.lunarlander.LLVisualizer;
import burlap.domain.singleagent.lunarlander.LunarLanderDomain;
import burlap.domain.singleagent.lunarlander.state.LLState;
import burlap.domain.singleagent.mountaincar.MountainCar;
import burlap.domain.singleagent.mountaincar.MountainCarVisualizer;
import burlap.mdp.auxiliary.common.ConstantStateGenerator;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.shell.visual.VisualExplorer;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import edu.gatech.churchill.cs7641.lunarlander.LunarLanderAnalysis;

public class MountainCarProblem {

    private MountainCar world;
    private SADomain singleAgentDomain;
    private SimpleHashableStateFactory hashingFactory;
    private State initialState;
    private SimulatedEnvironment simulatedEnvironment;

    public MountainCarProblem() {
        hashingFactory = new SimpleHashableStateFactory();
        world = new MountainCar();
        world.physParams.cosScale = 1.0;
        world.physParams.acceleration = 0.5;
        singleAgentDomain = world.generateDomain();

        initialState = world.valleyState();

        ConstantStateGenerator stateGenerator = new ConstantStateGenerator(initialState);
        simulatedEnvironment = new SimulatedEnvironment(singleAgentDomain, stateGenerator);
    }

    public MountainCarAnalysis createValueIterationAnalysis() {
        double maxDelta = 0.001;
        int maxIterations = 100;
        double gamma = 0.99;

        ValueIteration planner = new ValueIteration(singleAgentDomain, gamma, hashingFactory, maxDelta, maxIterations);
        Policy policy = planner.planFromState(initialState);

        return createAnalysis(planner, policy);
    }

    private MountainCarAnalysis createAnalysis(ValueFunction planner, Policy policy) {
        MountainCarAnalysis analysis = new MountainCarAnalysis();
        analysis.planner = planner;
        analysis.policy = policy;
        analysis.episode = PolicyUtils.rollout(policy, simulatedEnvironment);
        analysis.gui = new VisualExplorer(singleAgentDomain, MountainCarVisualizer.getVisualizer(world), initialState);

        return analysis;
    }

}
