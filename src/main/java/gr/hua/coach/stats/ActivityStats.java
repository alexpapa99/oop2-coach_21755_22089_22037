package gr.hua.coach.stats;

import java.time.Duration;

public class ActivityStats {

    private final Duration totalTime;
    private final double totalDistanceKm;
    private final double avgPaceMinPerKm;
    private final Double avgHeartRate; // nullable

    public ActivityStats(Duration totalTime,
                         double totalDistanceKm,
                         double avgPaceMinPerKm,
                         Double avgHeartRate) {
        this.totalTime = totalTime;
        this.totalDistanceKm = totalDistanceKm;
        this.avgPaceMinPerKm = avgPaceMinPerKm;
        this.avgHeartRate = avgHeartRate;
    }

    public Duration getTotalTime() {
        return totalTime;
    }

    public double getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public double getAvgPaceMinPerKm() {
        return avgPaceMinPerKm;
    }

    public Double getAvgHeartRate() {
        return avgHeartRate;
    }
}
