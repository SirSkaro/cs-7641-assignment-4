package edu.gatech.churchill.cs7641;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GeneralAnalysis {

    public List<Double> initialStateRewardPerIteration;
    public List<Double> initialStateRewardDeltaPerIteration;
    public List<Double> maxRewardDeltaPerIteration;
    public List<Long> timeInMsPerIteration;

    public GeneralAnalysis() {
        initialStateRewardPerIteration = new ArrayList<>();
        initialStateRewardDeltaPerIteration = new ArrayList<>();
        maxRewardDeltaPerIteration = new ArrayList<>();
        timeInMsPerIteration = new ArrayList<>();
    }

    public int iterationsToConverge() {
        return initialStateRewardPerIteration.size();
    }

    public void writeToFile(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename + ".csv");

        String initialStateRewards = initialStateRewardPerIteration.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String initialStateRewardDeltas = initialStateRewardDeltaPerIteration.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String maxRewardDeltas = initialStateRewardDeltaPerIteration.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String times = timeInMsPerIteration.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        writer.write(initialStateRewards);
        writer.write("\n");
        writer.write(initialStateRewardDeltas);
        writer.write("\n");
        writer.write(maxRewardDeltas);
        writer.write("\n");
        writer.write(times);
        writer.write("\n");
        writer.flush();
        writer.close();
    }

}
