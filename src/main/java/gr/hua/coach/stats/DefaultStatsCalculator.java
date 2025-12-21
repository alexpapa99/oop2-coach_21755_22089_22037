package gr.hua.coach.stats;

import gr.hua.coach.model.Activity;
import gr.hua.coach.model.Lap;
import gr.hua.coach.model.Track;
import gr.hua.coach.model.TrackPoint;

import java.time.Duration;

/**
 * Default implementation of the StatsCalculator interface.
 *
 * This class computes basic statistics such as total time, total distance,
 * average pace and average heart rate for an activity.
 */
public class DefaultStatsCalculator implements StatsCalculator {

    @Override
    public ActivityStats calculate(Activity activity, Double userWeight) {

        Duration totalTime = activity.getTotalTime();
        double totalDistanceKm = activity.getTotalDistance() / 1000.0;

        double avgPace = 0;
        if (totalDistanceKm > 0) {
            avgPace = totalTime.toMinutes() / totalDistanceKm;
        }

        int hrSum = 0;
        int hrCount = 0;

        for (Lap lap : activity.getLaps()) {
            for (Track track : lap.getTracks()) {
                for (TrackPoint p : track.getPoints()) {
                    if (p.getHeartRate() != null) {
                        hrSum += p.getHeartRate();
                        hrCount++;
                    }
                }
            }
        }

        Double avgHr = hrCount > 0 ? (double) hrSum / hrCount : null;

        return new ActivityStats(
                totalTime,
                totalDistanceKm,
                avgPace,
                avgHr
        );
    }
}
