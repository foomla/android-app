package org.foomla.androidapp.activities.exercisedetail;

import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.entities.twizard.Training;

import android.content.Context;
import android.content.Intent;

public class ExerciseDetailIntent extends Intent {

    private static final String EXTRA_EXERCISE = "extra.exercisedetail.exercise";
    private static final String EXTRA_TRAINING = "extra.exercisedetail.training";

    public ExerciseDetailIntent(Intent o) {
        super(o);
    }

    public ExerciseDetailIntent(Context context, Exercise exercise) {
        super(context, ExerciseDetailActivity.class);
        putExtra(EXTRA_EXERCISE, exercise);
    }

    public ExerciseDetailIntent(Context context, Training training, Exercise exercise) {
        super(context, ExerciseDetailActivity.class);
        putExtra(EXTRA_EXERCISE, exercise);
        putExtra(EXTRA_TRAINING, training);
    }

    public Exercise getExercise() {
        Object obj = getSerializableExtra(EXTRA_EXERCISE);
        return obj instanceof Exercise ? (Exercise) obj : null;
    }

    public Training getTraining() {
        Object obj = getSerializableExtra(EXTRA_TRAINING);
        return obj instanceof Training ? (Training) obj : null;
    }
}
