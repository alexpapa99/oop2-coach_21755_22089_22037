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

        List<File> tcxFiles = new ArrayList<>();
        Double weight = null;

        for (int i = 0; i < args.length; i++) {
            if ("-w".equals(args[i]) && i + 1 < args.length) {
                weight = Double.parseDouble(args[++i]);
            } else {
                tcxFiles.add(new File(args[i]));
            }
        }

        if (tcxFiles.isEmpty()) {
            System.out.println("No .tcx files provided.");
            return;
        }

        // TODO: parser + stats here
        System.out.println("Files to parse: " + tcxFiles.size());
        if (weight != null) {
            System.out.println("User weight: " + weight + " kg");
        }
    }

    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("java -jar coach.jar [-w weight] file1.tcx file2.tcx ...");
    }
}
