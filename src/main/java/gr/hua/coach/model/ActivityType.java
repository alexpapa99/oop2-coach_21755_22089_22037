package gr.hua.coach.model;

public enum ActivityType {
    RUNNING(0.12),
    WALKING(0.06),
    CYCLING(0.10),
    SWIMMING(0.13);

    private final double mu;

    ActivityType(double mu) {
        this.mu = mu;
    }

    public double getMu() {
        return mu;
    }
}
