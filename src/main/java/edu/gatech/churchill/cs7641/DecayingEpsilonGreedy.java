package edu.gatech.churchill.cs7641;

import burlap.behavior.policy.EpsilonGreedy;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;

public class DecayingEpsilonGreedy extends EpsilonGreedy {

    private double decay;
    private double originalEpsilon;

    public DecayingEpsilonGreedy(double epsilon, double decay) {
        super(epsilon);
        this.decay = decay;
        this.originalEpsilon = epsilon;
    }

    @Override
    public Action action(State s) {
        Action action = super.action(s);
        if(this.epsilon > 0.001) {
            this.epsilon = this.epsilon * decay;
        }
        return action;
    }

    public void resetEpsilon() {
        this.epsilon = originalEpsilon;
    }
}
