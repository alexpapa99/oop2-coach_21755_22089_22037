package gr.hua.coach.app;

import gr.hua.coach.model.Activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CoachApp {

    public static void main(String[] args) {
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

        // TODO: call parser
        // TODO: compute stats
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("java -jar coach.jar [-w weight] file1.tcx file2.tcx ...");
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


