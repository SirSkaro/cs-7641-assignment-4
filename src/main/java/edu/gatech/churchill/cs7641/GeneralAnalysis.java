package edu.gatech.churchill.cs7641;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GeneralAnalysis {

    public List<Double> rewardPerIteration;
    public List<Double> rewardDeltaPerIteration;
    public List<Long> timeInMsPerIteration;

    public GeneralAnalysis() {
        rewardPerIteration = new ArrayList<>();
        rewardDeltaPerIteration = new ArrayList<>();
        timeInMsPerIteration = new ArrayList<>();
    }

    public int iterationsToConverge() {
        return rewardPerIteration.size();
    }

    public void writeToFile(String filename) throws IOException {
        FileWriter writer = new FileWriter(filename + ".csv");

        String rewards = rewardPerIteration.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String rewardDelta = rewardDeltaPerIteration.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        String times = timeInMsPerIteration.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        writer.write(rewards);
        writer.write("\n");
        writer.write(rewardDelta);
        writer.write("\n");
        writer.write(times);
        writer.write("\n");
        writer.flush();
        writer.close();
    }

}
