package edu.gatech.churchill.cs7641.lunarlander;

import burlap.domain.singleagent.lunarlander.LLVisualizer;
import burlap.domain.singleagent.lunarlander.LunarLanderDomain;
import burlap.domain.singleagent.lunarlander.state.LLAgent;
import burlap.domain.singleagent.lunarlander.state.LLBlock;
import burlap.domain.singleagent.lunarlander.state.LLState;
import burlap.shell.visual.VisualExplorer;
import burlap.visualizer.Visualizer;

public class LunarLanderExperiment {

    private static final String VALUE_ITERATION = "VI";
    private static final String POLICY_ITERATION = "PI";
    private static final String Q_LEARNING = "QL";
    private static final String OUTPUT_DIRECTORY_PATH = "lunarlander";

    public static void main(String[] args) {
        //testWorld();
        if(args.length != 2) {
            printUsageMessage();
        }
        String algorithm = args[0];
        ProblemSize problemSize = args[1].equalsIgnoreCase("small") ? ProblemSize.SMALL : ProblemSize.LARGE;
        LunarLanderProblem problem = new LunarLanderProblem(problemSize);

        switch (algorithm) {
            case Q_LEARNING -> qLearningExperiment(problem);
            case VALUE_ITERATION -> valueIterationExperiment(problem);
            case POLICY_ITERATION -> policyIterationExperiment(problem);
            default -> printUsageMessage();
        }
    }

    private static void testWorld() {
        LLState initialState = new LLState(
                new LLAgent(8, 11, 0),
                new LLBlock.LLPad(12, 14, 0, 6, "goal"),
                new LLBlock.LLObstacle(7, 7, 0, 12, "left wall"),
                new LLBlock.LLObstacle(16, 16, 0, 12, "right wall"),
                new LLBlock.LLObstacle(7, 16, 12, 12, "ceiling"),
                new LLBlock.LLObstacle(10, 13, 7, 8, "floating rock"),
                new LLBlock.LLObstacle(9, 10, 0, 4, "cliff")
        );

        double thrust = 0.19;
        LunarLanderDomain world = new LunarLanderDomain();
        world.setXmin(7);
        world.setYmin(0);
        world.setYmax(12);
        world.setXmax(16);
        world.setGravity(-0.2);
        world.setAngmax(Math.PI/4);
        world.setAnginc(Math.PI/4);
        world.addThrustActionWithThrust(thrust);
        world.setTf(new LandedTerminalFunction(world));
        world.setRf(new GoalProximityRewardFunction(world, initialState.pad));


        Visualizer vis = LLVisualizer.getVisualizer(world);
        VisualExplorer exp = new VisualExplorer(world.generateDomain(), vis, initialState);

        exp.addKeyAction("w", LunarLanderDomain.ACTION_THRUST, String.valueOf(thrust));
        exp.addKeyAction("a", LunarLanderDomain.ACTION_TURN_LEFT, "");
        exp.addKeyAction("d", LunarLanderDomain.ACTION_TURN_RIGHT, "");
        exp.addKeyAction("x", LunarLanderDomain.ACTION_IDLE, "");

        exp.initGUI();
        while(true);
    }

    private static void valueIterationExperiment(LunarLanderProblem problem) {
        LunarLanderAnalysis analysis = problem.createValueIterationAnalysis();
        analysis.episode.write(String.format("%s/vi",OUTPUT_DIRECTORY_PATH));

        analysis.gui.initGUI();
    }

    private static void policyIterationExperiment(LunarLanderProblem problem) {
        LunarLanderAnalysis analysis = problem.createPolicyIterationAnalysis();
        analysis.episode.write(String.format("%s/pi",OUTPUT_DIRECTORY_PATH));

        analysis.gui.initGUI();
    }

    private static void qLearningExperiment(LunarLanderProblem problem) {
        LunarLanderAnalysis analysis = problem.createQLearningAnalysis();
        analysis.episode.write(String.format("%s/ql", OUTPUT_DIRECTORY_PATH));

        analysis.gui.initGUI();
    }

    private static void printUsageMessage() {
        System.err.println("Usage: [algorithm] [size]");
        System.err.println("Supported algorithms:");
        System.err.println(String.format("\t'%s' - Value Iteration", VALUE_ITERATION));
        System.err.println(String.format("\t'%s' - Policy Iteration", POLICY_ITERATION));
        System.err.println(String.format("\t'%s' - Q-Learning", Q_LEARNING));
        System.err.println("Supported sizes:");
        System.err.println("\t small, large");
        System.exit(1);
    }

}
