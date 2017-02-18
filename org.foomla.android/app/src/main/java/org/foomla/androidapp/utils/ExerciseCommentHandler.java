package org.foomla.androidapp.utils;

import android.content.Context;

import org.foomla.androidapp.async.RateExerciseTask;
import org.foomla.androidapp.data.Comment;
import org.foomla.api.client.FoomlaClient;
import org.foomla.api.client.entities.ExerciseRatingImpl;
import org.foomla.api.client.entities.UserImpl;
import org.foomla.api.entities.twizard.Exercise;

public class ExerciseCommentHandler {

    private final Context context;
    private final FoomlaClient foomlaClient;

    public ExerciseCommentHandler(Context context, FoomlaClient foomlaClient) {
        this.context = context;
        this.foomlaClient = foomlaClient;
    }

    public void handle(Exercise exercise, Comment comment, float rating) {
        ExerciseRatingImpl exerciseRating = new ExerciseRatingImpl();
        // exerciseRating.setExerciseId(exercise.getId());
        exerciseRating.setUserComment(comment.getValue());
        exerciseRating.setValue((int) rating);
        exerciseRating.setUser(new UserImpl(comment.getEmail()));

        new RateExerciseTask(context, foomlaClient).execute(exerciseRating);
    }
}
