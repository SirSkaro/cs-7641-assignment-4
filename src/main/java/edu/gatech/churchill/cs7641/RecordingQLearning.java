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

    public RecordingQLearning(SADomain domain, double gamma, HashableStateFactory hashingFactory, double qInit, double learningRate, Policy learningPolicy, int maxEpisodeSize) {
        super(domain, gamma, hashingFactory, qInit, learningRate, learningPolicy, maxEpisodeSize);
        analysis = new GeneralAnalysis();
        totalIterations = 0;
    }

    @Override
    public GreedyQPolicy planFromState(State initialState) {
        if (this.model == null) {
            throw new RuntimeException("QLearning cannot execute planFromState because a model is not specified.");
        } else {
            SimulatedEnvironment env = new SimulatedEnvironment(this.domain, initialState);
            int eCount = 0;
            if(totalIterations == 0) { // First time only
                analysis.rewardPerIteration.add(value(initialState));
                analysis.rewardDeltaPerIteration.add(0.0);
                analysis.timeInMsPerIteration.add(0L);
            }

            do {
                long startTime = System.currentTimeMillis();
                this.runLearningEpisode(env, this.maxEpisodeSize);
                long endTime = System.currentTimeMillis();

                analysis.rewardPerIteration.add(value(initialState));
                analysis.rewardDeltaPerIteration.add(analysis.rewardPerIteration.get(totalIterations+1) - analysis.rewardPerIteration.get(totalIterations));
                analysis.timeInMsPerIteration.add(endTime - startTime);

                ++eCount;
                totalIterations++;
            } while(eCount < this.numEpisodesForPlanning && this.maxQChangeInLastEpisode > this.maxQChangeForPlanningTermination);

            return new GreedyQPolicy(this);
        }
    }

    public GeneralAnalysis getAnalysis() {
        return analysis;
    }

}
