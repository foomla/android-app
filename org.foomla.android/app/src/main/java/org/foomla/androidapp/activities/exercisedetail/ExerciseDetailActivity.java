package org.foomla.androidapp.activities.exercisedetail;

import android.content.Intent;
import android.os.Bundle;

import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.BaseFragmentActivity;
import org.foomla.androidapp.activities.info.InfoActivity;
import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.domain.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExerciseDetailActivity extends BaseFragmentActivity implements ExerciseDetailFragment.ActionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExerciseDetailActivity.class);

    private ExerciseDetailFragment exerciseDetailFragment;

    @Override
    public Exercise getExercise() {
        Exercise exercise = new ExerciseDetailIntent(getIntent()).getExercise();
        if (exercise == null) {
            LOGGER.warn("No Exercise given!");
            finish();
        }

        return exercise;
    }

    @Override
    public Training getTraining() {
        return new ExerciseDetailIntent(getIntent()).getTraining();
    }

    @Override
    public void onCreate(final Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_exercisedetail);

        if (savedInstanceBundle == null) {
            exerciseDetailFragment = buildExerciseDetailFragment();
            getFragmentManager().beginTransaction().add(R.id.exercise_detail, exerciseDetailFragment).commit();
        }
    }

    @Override
    public void onShowHelp() {
        startActivity(new Intent(this, InfoActivity.class));
    }

    private ExerciseDetailFragment buildExerciseDetailFragment() {
        return new ExerciseDetailFragment();
    }
}
