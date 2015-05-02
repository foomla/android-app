package org.foomla.api.client;

import java.util.List;

import org.foomla.api.client.entities.ExerciseImpl;
import org.foomla.api.client.providers.ExerciseProvider;
import org.foomla.api.client.providers.ExerciseRatingProvider;
import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.entities.twizard.ExerciseRating;
import org.foomla.api.entities.twizard.TrainingPhase;

import org.junit.Test;

import junit.framework.Assert;

public class FoomlaClientWT {

    @Test
    public void testListExercises() throws Exception {
        FoomlaClient client = new FoomlaClient("http://localhost:8080/foomla-rest", null, null);
        ExerciseProvider provider = client.getProvider(ExerciseProvider.class);

        List<Exercise> exercises = provider.getAll();
        Assert.assertNotNull(exercises);
    }

    @Test
    public void testListExerciseByTrainingPhase() throws Exception {
        FoomlaClient client = new FoomlaClient("http://localhost:8080/foomla-rest", null, null);
        ExerciseProvider provider = client.getProvider(ExerciseProvider.class);

        List<Exercise> exercises = provider.getAllByTrainingPhase(TrainingPhase.MAIN);
        Assert.assertNotNull(exercises);
    }

    @Test
    public void testGetExerciseById() throws Exception {
        FoomlaClient client = new FoomlaClient("http://localhost:8080/foomla-rest", null, null);
        ExerciseProvider provider = client.getProvider(ExerciseProvider.class);

        Exercise exercise = provider.get(2);
        Assert.assertNotNull(exercise);
    }

    @Test
    public void testListExerciseRatingsByExercise() throws Exception {
        FoomlaClient client = new FoomlaClient("http://localhost:8080/foomla-rest", null, null);
        ExerciseRatingProvider provider = client.getProvider(ExerciseRatingProvider.class);

        Exercise exercise = new ExerciseImpl();
        exercise.setId(6);

        List<ExerciseRating> ratings = provider.getExerciseRating(exercise);
        Assert.assertNotNull(ratings);
    }
}
