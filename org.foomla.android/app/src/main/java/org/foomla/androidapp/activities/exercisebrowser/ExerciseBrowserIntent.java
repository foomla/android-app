package org.foomla.androidapp.activities.exercisebrowser;

import android.content.Context;
import android.content.Intent;

import org.foomla.androidapp.domain.Exercise;

public class ExerciseBrowserIntent extends Intent {

    public static final String EXTRA_TRAINING_PHASE = "extra.training.phase";
    public static final String EXTRA_EXERCISE = "extra.exercise";
    public static final int REQUEST_EXERCISE = 100;

    private Exercise exercise;

    public ExerciseBrowserIntent(Intent o) {
        super(o);
    }

	public ExerciseBrowserIntent(Context context) {
		super(context, ExerciseBrowserActivity.class);
	}

    public ExerciseBrowserIntent(Context context, int trainingPhase) {
        super(context, ExerciseBrowserActivity.class);
        putExtra(EXTRA_TRAINING_PHASE, trainingPhase);
    }

    public ExerciseBrowserIntent(Context context, Exercise exercise, int trainingPhase) {
        super(context, ExerciseBrowserActivity.class);
        putExtra(EXTRA_EXERCISE, exercise);
        putExtra(EXTRA_TRAINING_PHASE, trainingPhase);
    }

    public Integer getTrainingPhase() {
        int trainingPhase = getIntExtra(EXTRA_TRAINING_PHASE, -1);
        return trainingPhase >= 0 ? Integer.valueOf(trainingPhase) : null;
    }

    public Exercise getExercise() {
        Object obj = getSerializableExtra(EXTRA_EXERCISE);
        return obj instanceof Exercise ? (Exercise) obj : null;
    }
}
