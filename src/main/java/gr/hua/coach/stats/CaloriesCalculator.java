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
            Integer age
    ) {
        double base = calculateDefault(type, weightKg, duration);

        double factor = 1.0;

        if (gender != null) {
            String g = gender.trim().toLowerCase();
            if (g.equals("male")) factor += 0.10;    // +10%
            if (g.equals("female")) factor += 0.00;  // +0% (δηλ. τίποτα)
        }

        if (age != null) {
            if (age >= 40) factor -= 0.05; // -5%
            if (age < 18)  factor += 0.05; // +5%
        }

        return base * factor;
    }

   
    public static double calculateAlternative(
            ActivityType type,
            double weightKg,
            double minutes,
            String gender,
            Integer age
    ) {
        Duration duration = Duration.ofSeconds(Math.round(minutes * 60.0));
        return calculateAlternative(type, weightKg, duration, gender, age);
    }
}
