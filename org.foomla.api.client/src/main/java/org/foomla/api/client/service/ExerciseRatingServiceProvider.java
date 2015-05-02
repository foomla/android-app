package org.foomla.api.client.service;

import org.foomla.api.client.FoomlaClient.RestConnectionSettings;
import org.foomla.api.entities.twizard.ExerciseRating;
import org.foomla.api.services.twizard.ExerciseRatingList;

public class ExerciseRatingServiceProvider extends ServiceProvider implements
org.foomla.api.services.twizard.ExerciseRatingService {

    public ExerciseRatingServiceProvider(RestConnectionSettings settings) {
        super(settings);
    }

    @Override
    public void create(int exerciseId, ExerciseRating exerciseRating) {
        url("/exercise-ratings/{}", exerciseId).post(exerciseRating);
    }

    @Override
    public ExerciseRatingList getAllByExerciseId(int exerciseId) {
        return url("/exercise-ratings/{}", exerciseId).get().as(ExerciseRatingList.class);
    }

}
