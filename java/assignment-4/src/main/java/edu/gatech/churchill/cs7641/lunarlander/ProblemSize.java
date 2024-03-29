package edu.gatech.churchill.cs7641.lunarlander;

import burlap.behavior.policy.GreedyDeterministicQPolicy;
import burlap.behavior.policy.SolverDerivedPolicy;
import burlap.domain.singleagent.lunarlander.LunarLanderDomain;
import burlap.domain.singleagent.lunarlander.state.LLAgent;
import burlap.domain.singleagent.lunarlander.state.LLBlock;
import burlap.domain.singleagent.lunarlander.state.LLState;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.singleagent.oo.OOSADomain;
import edu.gatech.churchill.cs7641.DecayingEpsilonGreedy;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public enum ProblemSize {
    SMALL(1.0, 105, 1.0, 5.0, new GreedyDeterministicQPolicy(),
        () -> {
            return new LLState(
                    new LLAgent(4, 5, 0),
                    new LLBlock.LLPad(5, 6, 0, 2, "goal"),
                    new LLBlock.LLObstacle(3, 3, 0, 7, "left wall"),
                    new LLBlock.LLObstacle(7, 7, 0, 7, "right wall")
            );
        },
        (initialState) -> {
            LunarLanderDomain world = new LunarLanderDomain();
            world.setXmin(3);
            world.setYmin(0);
            world.setYmax(7);
            world.setXmax(7);
            world.setGravity(-0.2);
            world.setAngmax(Math.PI/4);
            world.setAnginc(Math.PI/4);
            world.setTf(new LandedTerminalFunction(world));
            world.setRf(new GoalProximityRewardFunction(world, initialState.pad));

            return world;
        },
        (world) -> {
            OOSADomain singleAgentDomain = world.generateDomain();
            singleAgentDomain.setActionTypes(new UniversalActionType(LunarLanderDomain.ACTION_TURN_LEFT))
                    .addActionType(new UniversalActionType(LunarLanderDomain.ACTION_TURN_RIGHT))
                    .addActionType(new LunarLanderDomain.ThrustType(List.of(0.15)));

            return singleAgentDomain;
        }
    ),
    LARGE(0.8, 500, 0.95, 1.0, new DecayingEpsilonGreedy(1.0, 0.999),
        () -> {
            return new LLState(
                    new LLAgent(8, 11, 0),
                    new LLBlock.LLPad(12, 14, 0, 4, "goal"),
                    new LLBlock.LLObstacle(7, 7, 0, 12, "left wall"),
                    new LLBlock.LLObstacle(16, 16, 0, 12, "right wall"),
                    new LLBlock.LLObstacle(7, 16, 12, 12, "ceiling"),
                    new LLBlock.LLObstacle(10, 13, 7, 8, "floating rock"),
                    new LLBlock.LLObstacle(9, 10, 0, 4, "cliff")
            );
        },
        (initialState) -> {
            LunarLanderDomain world = new LunarLanderDomain();
            world.setXmin(7);
            world.setYmin(0);
            world.setYmax(12);
            world.setXmax(16);
            world.setGravity(-0.2);
            world.setAngmax(Math.PI/4);
            world.setAnginc(Math.PI/4);
            world.setTf(new LandedTerminalFunction(world));
            world.setRf(new GoalProximityRewardFunction(world, initialState.pad));

            return world;
        },
        (world) -> {
            OOSADomain singleAgentDomain = world.generateDomain();
            singleAgentDomain.setActionTypes(new UniversalActionType(LunarLanderDomain.ACTION_TURN_LEFT))
                    .addActionType(new UniversalActionType(LunarLanderDomain.ACTION_TURN_RIGHT))
                    .addActionType(new LunarLanderDomain.ThrustType(List.of(0.15)));

            return singleAgentDomain;
        }
    );

    public final LLState initialState;
    public final LunarLanderDomain world;
    public final OOSADomain singleAgentDomain;
    public final double gamma;
    public final int qInit;
    public final double learningRate;
    public final double thresholdDelta;
    public final SolverDerivedPolicy explorationPolicy;

    ProblemSize(double gamma, int qInit, double learningRate, double thresholdDelta, SolverDerivedPolicy explorationPolicy,
                Supplier<LLState> initialStateSupplier,
                Function<LLState, LunarLanderDomain> worldSupplier,
                Function<LunarLanderDomain, OOSADomain> domainSupplier) {
        this.gamma = gamma;
        this.qInit = qInit;
        this.learningRate = learningRate;
        this.thresholdDelta = thresholdDelta;
        this.explorationPolicy = explorationPolicy;
        initialState = initialStateSupplier.get();
        world = worldSupplier.apply(initialState);
        singleAgentDomain = domainSupplier.apply(world);
    }

}
