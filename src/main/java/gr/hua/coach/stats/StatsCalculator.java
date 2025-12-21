package gr.hua.coach.stats;

import gr.hua.coach.model.Activity;

/**
 * Defines an interface for calculating statistics for an activity.
 *
 * Different implementations may provide different calculation strategies
 * (e.g. basic statistics, calorie-based statistics, heart-rate-based statistics).
 */
public interface StatsCalculator {

    ActivityStats calculate(Activity activity, Double userWeight);
}
