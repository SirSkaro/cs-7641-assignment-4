package edu.gatech.churchill.cs7641.lunarlander;

import burlap.domain.singleagent.lunarlander.LLVisualizer;
import burlap.domain.singleagent.lunarlander.LunarLanderDomain;
import burlap.domain.singleagent.lunarlander.LunarLanderRF;
import burlap.domain.singleagent.lunarlander.state.LLAgent;
import burlap.domain.singleagent.lunarlander.state.LLBlock;
import burlap.domain.singleagent.lunarlander.state.LLState;
import burlap.mdp.singleagent.SADomain;
import burlap.shell.visual.VisualExplorer;
import burlap.visualizer.Visualizer;

public class LunarLanderExperiment {

    private static final String VALUE_ITERATION = "VI";
    private static final String POLICY_ITERATION = "PI";
    private static final String Q_LEARNING = "QL";
    private static final String OUTPUT_DIRECTORY_PATH = "lunarlander";

    public static void main(String[] args) {
        testWorld();



        if(args.length != 1) {
            printUsageMessage();
        }
        String algorithm = args[0];
        LunarLanderProblem problem = new LunarLanderProblem();

        switch (algorithm) {
            case Q_LEARNING -> qLearningExperiment(problem);
            case VALUE_ITERATION -> valueIterationExperiment(problem);
//            case POLICY_ITERATION -> policyIterationExperiment(problem);
            default -> printUsageMessage();
        }
    }

    private static void testWorld() {
        LunarLanderDomain world = new LunarLanderDomain();
        world.setXmin(0);
        world.setYmin(0);
        world.setYmax(16);
        world.setXmax(16);
        world.setGravity(-0.2);
        world.setAngmax(Math.PI/4);
        world.setAnginc(Math.PI/4);
        world.addThrustActionWithThrust(0.19);
        world.setTf(new LandedTerminalFunction(world));
        world.setRf(new LunarLanderRF(world)); //TODO Make a reward function
        LLState initialState = new LLState(
                new LLAgent(8, 16, 0),
                new LLBlock.LLPad(9, 14, 0, 4, "goal")
                //new LLBlock.LLObstacle(60, 70, 0, 13, "obstacle")
        );

        Visualizer vis = LLVisualizer.getVisualizer(world);
        VisualExplorer exp = new VisualExplorer(world.generateDomain(), vis, initialState);

        exp.addKeyAction("w", LunarLanderDomain.ACTION_THRUST, "0.19");
        exp.addKeyAction("a", LunarLanderDomain.ACTION_TURN_LEFT, "");
        exp.addKeyAction("d", LunarLanderDomain.ACTION_TURN_RIGHT, "");
        exp.addKeyAction("x", LunarLanderDomain.ACTION_IDLE, "");

        exp.initGUI();
        while(true);
    }

    private static void valueIterationExperiment(LunarLanderProblem problem) {
        LunarLanderAnalysis analysis = problem.createValueIterationAnalysis();
        analysis.episode.write(String.format("%s/vi",OUTPUT_DIRECTORY_PATH));

        analysis.gui.setVisible(true);
        //problem.createVisualizer(OUTPUT_DIRECTORY_PATH);
    }

    private static void qLearningExperiment(LunarLanderProblem problem) {
        LunarLanderAnalysis analysis = problem.createQLearningAnalysis();
        analysis.episode.write(String.format("%s/ql", OUTPUT_DIRECTORY_PATH));

        analysis.gui.initGUI();
    }

    private static void printUsageMessage() {
        System.err.println("Usage: [algorithm]");
        System.err.println("Supported algorithms:");
        System.err.println(String.format("\t'%s' - Value Iteration", VALUE_ITERATION));
        System.err.println(String.format("\t'%s' - Policy Iteration", POLICY_ITERATION));
        System.err.println(String.format("\t'%s' - Q-Learning", Q_LEARNING));
        System.exit(1);
    }

}
