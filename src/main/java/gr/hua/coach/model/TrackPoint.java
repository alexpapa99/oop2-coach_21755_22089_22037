package gr.hua.coach.model;

import java.time.LocalDateTime;

public class TrackPoint {

    private final LocalDateTime time;
    private final double latitude;
    private final double longitude;
    private final double altitude;
    private final Integer heartRate; // nullable

    public TrackPoint(LocalDateTime time,
                      double latitude,
                      double longitude,
                      double altitude,
                      Integer heartRate) {
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.heartRate = heartRate;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    // Haversine formula
    public double distanceTo(TrackPoint other) {
        final int R = 6371000; // meters
        double lat1 = Math.toRadians(latitude);
        double lat2 = Math.toRadians(other.latitude);
        double dLat = lat2 - lat1;
        double dLon = Math.toRadians(other.longitude - longitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
