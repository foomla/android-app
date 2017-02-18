package org.foomla.androidapp.activities.exercisedetail;

import android.content.Intent;
import android.os.Bundle;

import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.BaseFragmentActivity;
import org.foomla.androidapp.activities.info.InfoActivity;
import org.foomla.androidapp.data.ExerciseRatingResult;
import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.domain.Training;
import org.foomla.androidapp.persistence.ExerciseRatingRepository;
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
        Training training = new ExerciseDetailIntent(getIntent()).getTraining();
        return training;
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
    public void onRatingChanged(final float rating) {
        /*
         * final FoomlaClient foomlaClient = ((FoomlaApplication) getApplication()).getFoomlaClient();
         * new RateExerciseHandler(this, foomlaClient).handle(getExercise(), rating);
         */
    }

    @Override
    public void onShowHelp() {
        startActivity(new Intent(this, InfoActivity.class));
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        updateRating(getExercise());
        updateComments(getExercise());
    }

    private ExerciseDetailFragment buildExerciseDetailFragment() {
        return new ExerciseDetailFragment();
    }

    private void updateComments(final Exercise exercise) {
    }

    private void updateRating(final Exercise exercise) {
        ExerciseRatingRepository repo = ExerciseRatingRepository.getInstance(this);
        ExerciseRatingResult rating = repo.getById(exercise.getId());

        if (rating != null) {
            exerciseDetailFragment.setRating(rating);
        }
    }
}
