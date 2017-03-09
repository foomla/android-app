package org.foomla.androidapp.activities.trainingdetail;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.BaseActivityWithNavDrawer;
import org.foomla.androidapp.activities.edittraining.EditTrainingActivity;
import org.foomla.androidapp.activities.exercisebrowser.ExerciseBrowserIntent;
import org.foomla.androidapp.activities.exercisedetail.ExerciseDetailIntent;
import org.foomla.androidapp.async.RepositoryLoadTask;
import org.foomla.androidapp.async.RepositorySaveTrainingTask;
import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.domain.Training;
import org.foomla.androidapp.persistence.Repository;
import org.foomla.androidapp.persistence.TrainingProxyRepository;
import org.foomla.androidapp.preferences.FoomlaPreferences;
import org.foomla.androidapp.preferences.FoomlaPreferences.Preference;
import org.foomla.androidapp.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TrainingDetailActivity extends BaseActivityWithNavDrawer implements TrainingDetailFragment.ActionHandler,
        ShakeDetector.Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingDetailActivity.class);

    protected ShakeDetector shakeDetector;
    protected Training training;
    protected TrainingDetailFragment trainingDetailFragment;

    @Override
    public Training getTraining() {
        if (training == null) {
            training = new TrainingDetailIntent(getIntent()).getTraining(getFoomlaApplication(), this);
        }

        return training;
    }

    @Override
    public void hearShake() {
        LOGGER.debug("Device shaked, create random training");

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(300);

        onRandomizeTrainingPhase(0);
        onRandomizeTrainingPhase(1);
        onRandomizeTrainingPhase(2);
        onRandomizeTrainingPhase(3);
        onRandomizeTrainingPhase(4);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // setSupportActionBar(toolbar);
        setTitle(getTraining() != null ? getTraining().getTitle() : getTitle());
        if (!isEditTrainingActivity()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initializeView();
    }

    @Override
    protected void initDrawer(Toolbar toolbar) {
        if (!isEditTrainingActivity()) {
            // skip drawer init
        } else {
            super.initDrawer(toolbar);
        }
    }

    protected void initializeView() {
        trainingDetailFragment = buildTrainingDetailFragment();
        getFragmentManager().beginTransaction().replace(R.id.training_detail_fragment, trainingDetailFragment)
                .commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_trainingdetail;
    }

    @Override
    public void onDisplayExerciseDetails(final int trainingPhase) {
        Exercise exercise = getTraining().getExercises().get(trainingPhase);
        Intent i = new ExerciseDetailIntent(this, getTraining(), exercise);
        startActivity(i);
    }

    @Override
    public void onEditTrainingPhase(final int trainingPhase) {
        Intent i = new ExerciseBrowserIntent(this, trainingPhase);
        startActivityForResult(i, ExerciseBrowserIntent.REQUEST_EXERCISE);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                if (!isEditTrainingActivity()) {
                    finish();
                    return true;
                }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRandomizeTrainingPhase(final int trainingPhase) {
        try {
            TrainingService service = ((FoomlaApplication) getApplication()).getTrainingService();
            Training training = service.random();

            Exercise exercise = training.getExercises().get(trainingPhase);
            getTraining().setExercise(trainingPhase, exercise);
            trainingDetailFragment.trainingChanged();
        } catch (IOException ioe) {
            LOGGER.error("Unable to set random training phase", ioe);
        }
    }

    @Override
    public void onSaveTraining() {
        Repository<Training> repository = TrainingProxyRepository.getInstance(getFoomlaApplication(), this);
        RepositoryLoadTask.LoadHandler<Training> handler = new RepositoryLoadTask.LoadHandler<Training>() {
            @Override
            public void handle(final Training training) {
                if (training != null) {
                    Toast.makeText(TrainingDetailActivity.this, R.string.training_saved, Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(TrainingDetailActivity.this, R.string.training_not_saved, Toast.LENGTH_LONG).show();
                }
            }
        };

        new RepositorySaveTrainingTask(TrainingDetailActivity.this, handler, repository).execute(getTraining());
    }

    protected TrainingDetailFragment buildTrainingDetailFragment() {
        return new TrainingDetailFragment();
    }

    protected void exerciseChanged(final Exercise exercise, final int trainingPhase) {
        LOGGER.debug("Exercise changed: " + trainingPhase + " -> " + exercise.getTitle());
        training.setExercise(trainingPhase, exercise);
        trainingDetailFragment.trainingChanged();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case ExerciseBrowserIntent.REQUEST_EXERCISE:

                    ExerciseBrowserIntent i = new ExerciseBrowserIntent(data);
                    exerciseChanged(i.getExercise(), i.getTrainingPhase());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        shakeDetector.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shakeDetector = new ShakeDetector(this);
        shakeDetector.start(sensorManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayExplainShakeToast();
    }

    private void displayExplainShakeToast() {
        if (!wasExplainShakeToastAlreadyDisplayed()) {
            Toast.makeText(this, R.string.dialog_explain_shake_random_exercises, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isEditTrainingActivity() {
        return this instanceof EditTrainingActivity;
    }

    private boolean wasExplainShakeToastAlreadyDisplayed() {
        boolean alreadyDisplayed = FoomlaPreferences.getBoolean(this,
                Preference.RANDOM_EXERCISE_ON_SHAKE_DIALOG_ALREADY_DISPLAYED);

        if (!alreadyDisplayed) {
            FoomlaPreferences.setBoolean(this, Preference.RANDOM_EXERCISE_ON_SHAKE_DIALOG_ALREADY_DISPLAYED, true);
        }

        return alreadyDisplayed;
    }
}
