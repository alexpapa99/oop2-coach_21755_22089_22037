package gr.hua.coach.stats;

import gr.hua.coach.model.ActivityType;

import java.time.Duration;

public class CaloriesCalculator {

   
    public static double calculateDefault(ActivityType type, double weightKg, Duration duration) {
        return Calories.estimate(type.getMu(), weightKg, duration);
    }

    
    public static double calculateDefault(ActivityType type, double weightKg, double minutes) {
        Duration duration = Duration.ofSeconds(Math.round(minutes * 60.0));
        return calculateDefault(type, weightKg, duration);
    }


    public static double calculateAlternative(
            ActivityType type,
            double weightKg,
            Duration duration,
            String gender,
            Integer age,
            Double avgHeartRate
    ) {
        if (avgHeartRate == null || age == null || gender == null) {
            return calculateDefault(type, weightKg, duration);
        }

        double h = avgHeartRate;
        double w = weightKg;
        double a = age;
        double t = duration.toSeconds() / 60.0; // minutes

        String g = gender.trim().toLowerCase();
        double calories;

        if (g.equals("male")) {
            calories = (-55.0969 + (0.6309 * h) + (0.1966 * w) + (0.2017 * a))
                    * t / 4.184;
        } else if (g.equals("female")) {
            calories = (-20.4022 + (0.4472 * h) + (0.1263 * w) + (0.074 * a))
                    * t / 4.184;
        } else {
            return calculateDefault(type, weightKg, duration);
        }

        return Math.max(0, calories);
    }


    public static double calculateAlternative(
            ActivityType type,
            double weightKg,
            double minutes,
            String gender,
            Integer age,
            Double avgHeartRate
    ) {
        Duration duration = Duration.ofSeconds(Math.round(minutes * 60.0));
        return calculateAlternative(
                type, weightKg, duration, gender, age, avgHeartRate
        );
    }
}
