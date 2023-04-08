package edu.gatech.churchill.cs7641;

import burlap.behavior.policy.GreedyQPolicy;
import burlap.behavior.policy.Policy;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.statehashing.HashableStateFactory;

public class RecordingQLearning extends QLearning {

    private GeneralAnalysis analysis;
    private int totalIterations;
    private double thresholdDelta;

    public RecordingQLearning(SADomain domain, double gamma, HashableStateFactory hashingFactory, double qInit, double learningRate, Policy learningPolicy, int maxEpisodeSize, double thresholdDelta) {
        super(domain, gamma, hashingFactory, qInit, learningRate, learningPolicy, maxEpisodeSize);
        analysis = new GeneralAnalysis();
        totalIterations = 0;
        this.thresholdDelta = thresholdDelta;
    }

    @Override
    public GreedyQPolicy planFromState(State initialState) {
        double delta = Double.MAX_VALUE;
        while(!hasConverged()) {
            SimulatedEnvironment env = new SimulatedEnvironment(this.domain, initialState);
            if(totalIterations == 0) { // First time only
                analysis.initialStateRewardPerIteration.add(value(initialState));
                analysis.initialStateRewardDeltaPerIteration.add(0.0);
                analysis.maxRewardDeltaPerIteration.add(0.0);
                analysis.timeInMsPerIteration.add(0L);
            }


            long startTime = System.currentTimeMillis();
            this.runLearningEpisode(env, this.maxEpisodeSize);
            long endTime = System.currentTimeMillis();

            analysis.initialStateRewardPerIteration.add(value(initialState));
            analysis.initialStateRewardDeltaPerIteration.add(analysis.initialStateRewardPerIteration.get(totalIterations+1) - analysis.initialStateRewardPerIteration.get(totalIterations));
            analysis.maxRewardDeltaPerIteration.add(maxQChangeInLastEpisode);
            analysis.timeInMsPerIteration.add(endTime - startTime);

            totalIterations++;
            delta = maxQChangeInLastEpisode;
        }

        if(totalIterations > maxEpisodeSize) {
            System.out.println("Hit the maximum number of episodes");
        } else {
            System.out.println("Hit the delta threshold");
        }

        return new GreedyQPolicy(this);
    }

    private boolean hasConverged() {
        if(analysis.maxRewardDeltaPerIteration.size() < 3) {
            return false;
        }
        boolean lastThreeDeltasUnderThreshold = analysis.maxRewardDeltaPerIteration.get(totalIterations - 2) < thresholdDelta
                && analysis.maxRewardDeltaPerIteration.get(totalIterations - 1) < thresholdDelta
                && analysis.maxRewardDeltaPerIteration.get(totalIterations) < thresholdDelta;
        boolean hitMaxIterations = totalIterations > maxEpisodeSize;

        return lastThreeDeltasUnderThreshold || hitMaxIterations;
    }

    public GeneralAnalysis getAnalysis() {
        return analysis;
    }

}
