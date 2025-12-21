package gr.hua.coach.stats;

import java.time.Duration;

/**
 * Immutable data class that holds calculated statistics for an activity.
 *
 * Instances of this class contain aggregated values such as total time,
 * total distance, average pace and average heart rate.
 */
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

    public static String formatDuration(Duration d) {
        long seconds = d.getSeconds();
        long minutes = seconds / 60;
        long remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
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
