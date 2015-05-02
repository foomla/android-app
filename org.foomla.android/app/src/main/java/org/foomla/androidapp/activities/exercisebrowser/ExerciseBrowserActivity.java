package org.foomla.androidapp.activities.exercisebrowser;

import android.content.Intent;
import android.os.Bundle;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.BaseActivityWithNavDrawer;
import org.foomla.androidapp.activities.exercisedetail.ExerciseDetailIntent;
import org.foomla.androidapp.async.DownloadExercisesTask;
import org.foomla.androidapp.async.DownloadTask;
import org.foomla.androidapp.async.DownloadTrainingPhaseExercisesTask;
import org.foomla.api.client.FoomlaClient;
import org.foomla.api.entities.twizard.Exercise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    public void onCreate(final Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_exercisebrowser);
        createNavDrawer();

        exercises = new ArrayList<Exercise>();
        trainingPhase = getTrainingPhaseFromIntent();

        exerciseBrowserFragment = new ExerciseBrowserFragment();
        getFragmentManager().beginTransaction().replace(R.id.exercise_browser, exerciseBrowserFragment).commit();

        initialize();
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

    private Integer getTrainingPhaseFromIntent() {
        Intent i = getIntent();
        Integer trainingPhase = i.getIntExtra(ExerciseBrowserIntent.EXTRA_TRAINING_PHASE, -1);
        return trainingPhase != null && trainingPhase >= 0 ? trainingPhase : null;
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

        DownloadTask.DownloadHandler<List<Exercise>> handler = new DownloadTask.DownloadHandler<List<Exercise>>() {
            @Override
            public void handle(final List<Exercise> exercises) {
                setExercises(exercises);
            }
        };

        FoomlaClient foomlaClient = ((FoomlaApplication) getApplication()).getFoomlaClient();
        new DownloadExercisesTask(this, handler) {
            @Override
            protected int getLoadingTextId() {
                return R.string.exercises;
            }
            ;
        }.execute(foomlaClient);
    }

    private void initializeWithTrainingPhase(final int trainingPhase) {
        LOGGER.info("Initialize with exercises for training phase '" + trainingPhase + "'");

        DownloadTask.DownloadHandler<List<Exercise>> handler = new DownloadTask.DownloadHandler<List<Exercise>>() {
            @Override
            public void handle(final List<Exercise> exercises) {
                setExercises(exercises);
            }
        };

        FoomlaClient foomlaClient = ((FoomlaApplication) getApplication()).getFoomlaClient();
        new DownloadTrainingPhaseExercisesTask(this, handler, trainingPhase) {
            @Override
            protected int getLoadingTextId() {
                return R.string.exercises;
            }
            ;
        }.execute(foomlaClient);
    }

    private void setExercises(final List<Exercise> exercises) {
        if (exercises != null) {
            LOGGER.info("Initialize with " + exercises.size() + " exercises");
            this.exercises = exercises;
            exerciseBrowserFragment.notifyDataChanged();
        }
    }
}
