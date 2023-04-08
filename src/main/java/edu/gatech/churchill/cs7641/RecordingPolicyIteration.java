package edu.gatech.churchill.cs7641;

import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.statehashing.HashableStateFactory;

public class RecordingPolicyIteration extends PolicyIteration {

    private GeneralAnalysis analysis;

    public RecordingPolicyIteration(SADomain domain, double gamma, HashableStateFactory hashingFactory, double maxDelta, int maxEvaluationIterations, int maxPolicyIterations) {
        super(domain, gamma, hashingFactory, maxDelta, maxEvaluationIterations, maxPolicyIterations);
        analysis = new GeneralAnalysis();
    }

    @Override
    public GreedyQPolicy planFromState(State initialState) {
        int iterations = 0;
        if (this.performReachabilityFrom(initialState) || !this.hasRunPlanning) {
            analysis.rewardPerIteration.add(value(initialState));
            analysis.rewardDeltaPerIteration.add(0.0);
            analysis.timeInMsPerIteration.add(0L);

            while(true) {
                long startTime = System.currentTimeMillis();
                double delta = this.evaluatePolicy();
                this.evaluativePolicy = new GreedyQPolicy(this.getCopyOfValueFunction());
                long endTime = System.currentTimeMillis();

                analysis.rewardPerIteration.add(value(initialState));
                analysis.rewardDeltaPerIteration.add(analysis.rewardPerIteration.get(iterations+1) - analysis.rewardPerIteration.get(iterations));
                analysis.timeInMsPerIteration.add(endTime - startTime);

                ++iterations;
                if (!(delta > this.maxPIDelta) || iterations >= this.maxPolicyIterations) {
                    this.hasRunPlanning = true;
                    break;
                }
            }
        }

        this.totalPolicyIterations += iterations;
        return (GreedyQPolicy)this.evaluativePolicy;
    }



    public GeneralAnalysis getAnalysis() {
        return analysis;
    }

}
