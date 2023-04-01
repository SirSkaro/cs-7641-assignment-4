package edu.gatech.churchill.cs7641.gridworld;

import java.util.List;

public class GridWorldExperiment {

    public static void main(String args[]) {
        int width = 11, height = 11;
        float probabilityOfSuccessfulTransition = 0.8f;

        List<BaseGridWorldProblem> problems = List.of(
                new QLearningGridWorldProblem(width, height, probabilityOfSuccessfulTransition)
        );

        problems.forEach(problem -> {
            var experiment = problem.createExperiment();
            experiment.startExperiment();
        });
    }

}
