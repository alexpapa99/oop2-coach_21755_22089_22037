package gr.hua.coach.stats;

import gr.hua.coach.model.Activity;

public interface StatsCalculator {

    ActivityStats calculate(Activity activity, Double userWeight);
}
