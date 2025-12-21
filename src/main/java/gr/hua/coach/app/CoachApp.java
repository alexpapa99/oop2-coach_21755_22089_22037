package gr.hua.coach.app;

import gr.hua.coach.model.Activity;

import gr.hua.coach.stats.ActivityStats;
import gr.hua.coach.stats.StatsCalculator;
import gr.hua.coach.stats.DefaultStatsCalculator;
import gr.hua.coach.stats.Calories;

import gr.hua.coach.parser.TcxActivityParser;
import gr.hua.coach.parser.ActivityParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CoachApp {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            printUsage();
            return;
        }

        CliOptions options = parseArgs(args);

        if (options.files.isEmpty()) {
            System.out.println("No .tcx files provided.");
            return;
        }

        System.out.println("Files to parse: " + options.files.size());
        if (options.weight != null) {
            System.out.println("User weight: " + options.weight + " kg");
        }

        List<Activity> allActivities = new ArrayList<>();

        ActivityParser parser = new TcxActivityParser();
        StatsCalculator statsCalculator = new DefaultStatsCalculator();

        for (File file : options.files) {
            // TODO: replace with real parser
             List<Activity> activitiesFromFile = parser.parse(file);
             allActivities.addAll(activitiesFromFile);
        }

        System.out.println("Total activities loaded: " + allActivities.size());
        System.out.println();

        for (Activity activity : allActivities) {
             ActivityStats stats = statsCalculator.calculate(activity, options.weight);
             printActivity(activity, stats, options.weight);

        }

    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("java -jar coach.jar [-w weight] file1.tcx file2.tcx ...");
    }

    private static void printActivity(Activity activity, ActivityStats stats, Double weight) {

    System.out.println("Activity: " + activity.getType());

    System.out.println("Total Time: " + ActivityStats.formatDuration(stats.getTotalTime()));
    System.out.println("Total Distance: " + stats.getTotalDistanceKm() + " km");

    // Για running δείχνουμε pace (όπως στο Μέρος 1)
    System.out.println("Avg Pace: " + stats.getAvgPaceMinPerKm() + " min/km");

    if (stats.getAvgHeartRate() != null) {
        System.out.println("Avg Heart Rate: " + stats.getAvgHeartRate() + " bpm");
    }

    // calories an yparxei varos
    

    if (weight != null) {

        double kcal = Calories.estimate(
                activity.getType().getMu(),
                weight,
                stats.getTotalTime()
        );
        System.out.printf("Calories: %.0f kcal%n", kcal);
    }

    System.out.println();
}




    private static CliOptions parseArgs(String[] args) {
        CliOptions options = new CliOptions();

        for (int i = 0; i < args.length; i++) {
            if ("-w".equals(args[i]) && i + 1 < args.length) {
                options.weight = Double.parseDouble(args[++i]);
            } else {
                options.files.add(new File(args[i]));
            }
        }
        return options;
    }

    private static class CliOptions {
        List<File> files = new ArrayList<>();
        Double weight;
    }

}


