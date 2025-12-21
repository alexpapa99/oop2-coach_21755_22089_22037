package gr.hua.coach.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a lap within an activity.
 *
 * A lap is a logical subdivision of an activity and consists of one or more tracks.
 * This class aggregates duration and distance from its tracks.
 */
public class Lap {

    private final List<Track> tracks;

    public Lap() {
        this.tracks = new ArrayList<>();
    }

    public void addTrack(Track track) {
        this.tracks.add(track);
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public Duration getDuration() {
        return tracks.stream()
                .map(Track::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    public double getDistance() {
        return tracks.stream()
                .mapToDouble(Track::getDistance)
                .sum();
    }
}
