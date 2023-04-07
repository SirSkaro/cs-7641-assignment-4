package edu.gatech.churchill.cs7641;

import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.statehashing.HashableState;
import burlap.statehashing.HashableStateFactory;

import java.util.Iterator;
import java.util.Set;

public class RecordingValueIteration extends ValueIteration {

    private GeneralAnalysis analysis;
    private State initialState;

    public RecordingValueIteration(SADomain domain, double gamma, HashableStateFactory hashingFactory, double maxDelta, int maxIterations) {
        super(domain, gamma, hashingFactory, maxDelta, maxIterations);
        analysis = new GeneralAnalysis();
    }

    @Override
    public GreedyQPolicy planFromState(State initialState) {
        this.initialState = initialState;
        return super.planFromState(initialState);
    }

    @Override
    public void runVI() {
        if (!this.foundReachableStates) {
            throw new RuntimeException("Cannot run VI until the reachable states have been found. Use the planFromState or performReachabilityFrom method at least once before calling runVI.");
        } else {

            Set<HashableState> states = this.valueFunction.keySet();
            analysis.rewardPerIteration.add(value(initialState));
            analysis.rewardDeltaPerIteration.add(0.0);
            analysis.timeInMsPerIteration.add(0L);

            int i;
            for(i = 0; i < this.maxIterations; ++i) {
                long startTime = System.currentTimeMillis();
                double delta = 0.0;
                double v;
                double maxQ;

                for(Iterator<HashableState> var5 = states.iterator(); var5.hasNext(); delta = Math.max(Math.abs(maxQ - v), delta)) {
                    HashableState sh = var5.next();
                    v = this.value(sh);
                    maxQ = this.performBellmanUpdateOn(sh);
                }
                long endTime = System.currentTimeMillis();

                // record statistics
                analysis.rewardPerIteration.add(value(initialState));
                analysis.rewardDeltaPerIteration.add(analysis.rewardPerIteration.get(i+1) - analysis.rewardPerIteration.get(i));
                analysis.timeInMsPerIteration.add(endTime - startTime);

                if (delta < this.maxDelta) {
                    break;
                }
            }

            this.hasRunVI = true;
        }
    }

    public GeneralAnalysis getAnalysis() {
        return analysis;
    }

}
