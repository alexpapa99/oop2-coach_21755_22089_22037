package gr.hua.coach.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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
