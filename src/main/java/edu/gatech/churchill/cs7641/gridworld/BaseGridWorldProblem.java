package edu.gatech.churchill.cs7641.gridworld;

import burlap.behavior.singleagent.auxiliary.performance.LearningAlgorithmExperimenter;
import burlap.domain.singleagent.gridworld.GridWorldDomain;
import burlap.domain.singleagent.gridworld.state.GridAgent;
import burlap.domain.singleagent.gridworld.state.GridLocation;
import burlap.domain.singleagent.gridworld.state.GridWorldState;
import burlap.mdp.auxiliary.common.SinglePFTF;
import burlap.mdp.auxiliary.stateconditiontest.TFGoalCondition;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.singleagent.common.GoalBasedRF;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;

public abstract class BaseGridWorldProblem {

    private static final float GOAL_REWARD = 5;
    private static final float NON_GOAL_REWARD = -0.1f;

    protected OOSADomain domain;
    protected GridWorldState initialState;

    protected BaseGridWorldProblem(int width, int height, float probabilityOfSuccessfulTransition) {
        GridWorldDomain gridWorld = new GridWorldDomain(width, height);
        gridWorld.setMapToFourRooms();
        gridWorld.setProbSucceedTransitionDynamics(probabilityOfSuccessfulTransition);

        TerminalFunction terminalFunction = new SinglePFTF(PropositionalFunction.findPF(gridWorld.generatePfs(), GridWorldDomain.PF_AT_LOCATION));
        RewardFunction rewardFunction = new GoalBasedRF(new TFGoalCondition(terminalFunction), GOAL_REWARD, NON_GOAL_REWARD);

        gridWorld.setTf(terminalFunction);
        gridWorld.setRf(rewardFunction);

        domain = gridWorld.generateDomain(); // Don't know if the order of these functions matters

        initialState = new GridWorldState(new GridAgent(0, 0), new GridLocation(width - 1, height - 1, "loc0"));
    }

    abstract LearningAlgorithmExperimenter createExperiment();

}
