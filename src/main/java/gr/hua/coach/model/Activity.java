package gr.hua.coach.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single athletic activity, such as running, cycling or swimming.
 *
 * An Activity has a specific type, a start time and consists of one or more laps.
 * Each lap may contain one or more tracks which in turn contain track points.
 *
 * This class provides basic aggregation methods for computing total time,
 * total distance and average speed.
 */
public class Activity {

    private final ActivityType type;
    private final LocalDateTime startTime;
    private final List<Lap> laps;

    public Activity(ActivityType type, LocalDateTime startTime) {
        this.type = type;
        this.startTime = startTime;
        this.laps = new ArrayList<>();
    }

    public ActivityType getType() {
        return type;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public List<Lap> getLaps() {
        return laps;
    }

    public void addLap(Lap lap) {
        this.laps.add(lap);
    }

    // -------- Calculations --------

    public Duration getTotalTime() {
        return laps.stream()
                .map(Lap::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public double getTotalDistance() {
        return laps.stream()
                .mapToDouble(Lap::getDistance)
                .sum();
    }

    public double getAverageSpeedKmh() {
        double hours = getTotalTime().toSeconds() / 3600.0;
        if (hours == 0) return 0;
        return (getTotalDistance() / 1000.0) / hours;
    }
}
