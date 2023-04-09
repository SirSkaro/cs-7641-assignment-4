package edu.gatech.churchill.cs7641.lunarlander;

import burlap.domain.singleagent.lunarlander.LunarLanderDomain;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.state.State;

import java.util.List;

public class LandedTerminalFunction implements TerminalFunction {
    private List<PropositionalFunction> terminalFunctions;

    public LandedTerminalFunction(LunarLanderDomain world){
        terminalFunctions = List.of(world.new TouchPadPF("touching goal"),
                world.new TouchGroundPF("touching ground"),
                world.new TouchSurfacePF("touching obstacle"));
    }

    @Override
    public boolean isTerminal(State s) {
        return terminalFunctions.stream().anyMatch(pf -> pf.someGroundingIsTrue((OOState) s));
    }
}
