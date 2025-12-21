package gr.hua.coach.stats;

import java.time.Duration;

public final class Calories {

    private Calories() {
        // utility class
    }

    // C = μ * w * t (t se wres)
    public static double estimate(double mu, double weightKg, Duration totalTime) {
        double hours = totalTime.toSeconds() / 3600.0;
        return mu * weightKg * hours;
    }
}
