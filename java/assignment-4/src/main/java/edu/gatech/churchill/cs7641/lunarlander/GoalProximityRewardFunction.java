package edu.gatech.churchill.cs7641.lunarlander;

import burlap.domain.singleagent.lunarlander.LunarLanderDomain;
import burlap.domain.singleagent.lunarlander.state.LLBlock;
import burlap.domain.singleagent.lunarlander.state.LLState;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;

public class GoalProximityRewardFunction implements RewardFunction {

    private PropositionalFunction onGoal;
    private PropositionalFunction touchingGoal;
    private PropositionalFunction onGround;
    private PropositionalFunction touchingWall;
    private LLBlock.LLPad goal;

    public GoalProximityRewardFunction(LunarLanderDomain world, LLBlock.LLPad goal) {
        onGoal = world.new OnPadPF("on goal");
        touchingGoal = world.new TouchPadPF("touching goal");
        onGround = world.new TouchGroundPF("touching ground");
        touchingWall = world.new TouchSurfacePF("touching wall");

        this.goal = goal;
    }

    @Override
    public double reward(State s, Action action, State sPrime) {
        OOState castedSPrime = (OOState) sPrime;

        if(onGoal.someGroundingIsTrue(castedSPrime)) {
            return 1000;
        } else if(touchingGoal.someGroundingIsTrue(castedSPrime)) {
            return 100;
        } else if(onGround.someGroundingIsTrue(castedSPrime) || touchingWall.someGroundingIsTrue(castedSPrime)) {
            return -1000;
        }  else {
            LLState llstate = (LLState) sPrime;
            double centerGoalXCoordinate = (goal.right + goal.left) / 2;
            double goalYCoordinate = goal.top;
            double xDelta = llstate.agent.x - centerGoalXCoordinate;
            double yDelta = llstate.agent.y - goalYCoordinate;

            return -(Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2)));
        }
    }
}
