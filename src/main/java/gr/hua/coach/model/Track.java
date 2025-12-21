package gr.hua.coach.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a track within a lap.
 *
 * A track consists of an ordered list of track points.
 * Duration and distance are calculated based on the first and last points
 * and the distances between consecutive points.
 */
public class Track {

    private final List<TrackPoint> points;

    public Track() {
        this.points = new ArrayList<>();
    }

    public void addPoint(TrackPoint point) {
        this.points.add(point);
    }

    public List<TrackPoint> getPoints() {
        return points;
    }

    public Duration getDuration() {
        if (points.size() < 2) return Duration.ZERO;
        return Duration.between(
                points.get(0).getTime(),
                points.get(points.size() - 1).getTime()
        );
    }

    public double getDistance() {
        double distance = 0;
        for (int i = 1; i < points.size(); i++) {
            distance += points.get(i - 1).distanceTo(points.get(i));
        }
        return distance;
    }
}
