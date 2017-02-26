package org.foomla.androidapp.activities.exercisebrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.BaseActivityWithNavDrawer;
import org.foomla.androidapp.activities.exercisedetail.ExerciseDetailIntent;
import org.foomla.androidapp.activities.exercisefilter.ExerciseFilterActivity;
import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.domain.TrainingPhase;
import org.foomla.androidapp.service.ExerciseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExerciseBrowserActivity extends BaseActivityWithNavDrawer
        implements ExerciseBrowserFragment.FragmentCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExerciseBrowserActivity.class);

    private ExerciseBrowserFragment exerciseBrowserFragment;

    private List<Exercise> exercises;

    private Integer trainingPhase;

    @Override
    public List<Exercise> getExercises() {
        return exercises;
    }

    @Override
    public boolean isShowSelectButton() {
        return trainingPhase != null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exercisebrowser;
    }

    @Override
    public void onCreate(final Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);

        exercises = new ArrayList<>();
        trainingPhase = getTrainingPhaseFromIntent();

        exerciseBrowserFragment = new ExerciseBrowserFragment();
        getFragmentManager().beginTransaction().replace(R.id.exercise_browser, exerciseBrowserFragment).commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.exercisebrowser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter:
                startFilterActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSelectExercise(final Exercise exercise) {
        Integer trainingPhase = getTrainingPhaseFromIntent();
        Intent i = new ExerciseBrowserIntent(this, exercise, trainingPhase);
        setResult(RESULT_OK, i);
        finish();
    }

    @Override
    public void onShowExerciseDetails(final Exercise exercise) {
        Intent i = new ExerciseDetailIntent(this, exercise);
        startActivity(i);
    }

    private void startFilterActivity() {
        if (getFoomlaApplication().isProVersion()) {
            Intent intent = new Intent(this, ExerciseFilterActivity.class);
            startActivity(intent);
        } else {
            Snackbar.make(
                    findViewById(android.R.id.content),
                    "TODO: Inform user about PRO version",
                    Snackbar.LENGTH_LONG).show();
        }
    }

    private Integer getTrainingPhaseFromIntent() {
        Intent i = getIntent();
        Integer trainingPhase = i.getIntExtra(ExerciseBrowserIntent.EXTRA_TRAINING_PHASE, -1);
        return trainingPhase >= 0 ? trainingPhase : null;
    }

    private void initialize() {
        if (trainingPhase != null) {
            initializeWithTrainingPhase(trainingPhase);
        } else {
            initializeWithAllExercises();
        }
    }

    private void initializeWithAllExercises() {
        LOGGER.info("Initialize with all exercises");

        try {
            ExerciseService service = ((FoomlaApplication) getApplication()).getExerciseService();
            setExercises(service.list());
        } catch (IOException ioe) {
            LOGGER.error("Unable to read exercises from JSON", ioe);
            // TODO inform user
        }
    }

    private void initializeWithTrainingPhase(final int trainingPhase) {
        LOGGER.info("Initialize with exercises for training phase '" + trainingPhase + "'");

        try {
            ExerciseService service = ((FoomlaApplication) getApplication()).getExerciseService();
            setExercises(service.filter(TrainingPhase.getById(trainingPhase)));
        } catch (IOException ioe) {
            LOGGER.error("Unable to read exercises from JSON", ioe);
            // TODO inform user
        }
    }

    private void setExercises(final List<Exercise> exercises) {
        if (exercises != null) {
            LOGGER.info("Initialize with " + exercises.size() + " exercises");
            this.exercises = exercises;
            exerciseBrowserFragment.notifyDataChanged();
        }
    }
}
