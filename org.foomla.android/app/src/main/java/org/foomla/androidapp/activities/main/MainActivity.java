package org.foomla.androidapp.activities.main;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.view.View;

import com.squareup.seismic.ShakeDetector;

import org.foomla.androidapp.BuildConfig;
import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.BaseActivityWithNavDrawer;
import org.foomla.androidapp.activities.edittraining.EditTrainingActivity;
import org.foomla.androidapp.activities.exercisedetail.ExerciseDetailIntent;
import org.foomla.androidapp.activities.mytrainings.MyTrainingsActivity;
import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.preferences.FoomlaPreferences;
import org.foomla.androidapp.preferences.FoomlaPreferences.Preference;
import org.foomla.androidapp.service.ExerciseService;
import org.foomla.androidapp.widgets.ExplainShakeDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MainActivity extends BaseActivityWithNavDrawer implements MainFragment.ActionHandler,
        ShakeDetector.Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainActivity.class);

    private static final int TWO_SECONDS = 2000;

    private MainFragment mainFragment;
    private ShakeDetector shakeDetector;

    @Override
    public void hearShake() {
        LOGGER.info("Device shaked, start random training");

        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(300);

        startActivity(new Intent(this, EditTrainingActivity.class));
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainFragment = new MainFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_main, mainFragment).commit();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onLoadLatestExercise() {
        try {
            ExerciseService service = ((FoomlaApplication) getApplication()).getExerciseService();
            mainFragment.showLatestExercise(service.random());
        } catch (IOException ioe) {
            LOGGER.error("Unable to set random exercise", ioe);
            // TODO inform user
        }
    }

    @Override
    public void onOpenWebsite() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.website_url))));
    }

    @Override
    public void onShowEditTrainingActivity() {
        startActivity(new Intent(this, EditTrainingActivity.class));
    }

    @Override
    public void onShowExerciseDetailActivity(final Exercise exercise) {
        startActivity(new ExerciseDetailIntent(this, exercise));
    }

    @Override
    public void onShowMyTrainingsActivity() {
        startActivity(new Intent(this, MyTrainingsActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        shakeDetector.stop();
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        showNavDrawerIfAppIsUsedTheFirstTime();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shakeDetector = new ShakeDetector(this);
        shakeDetector.start(sensorManager);

        if (getFoomlaApplication().isProVersion() && BuildConfig.DEBUG) {
            Snackbar.make(findViewById(android.R.id.content), "DEBUG: PRO VERSION!", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayExplainShakeDialog();
    }

    private void displayExplainShakeDialog() {
        View dialog = findViewById(R.id.dialog_explain_shake);
        if (dialog == null && !wasExplainShakeDialogAlreadyDisplayed()) {
            ExplainShakeDialog.display(this, R.string.dialog_explain_shake_random_training);
        }
    }

    private void showNavDrawerIfAppIsUsedTheFirstTime() {
        boolean navDrawerAlreadyUsed = FoomlaPreferences.getBoolean(this, Preference.NAV_DRAWER_ALREADY_USED);
        if (!navDrawerAlreadyUsed) {
            FoomlaPreferences.setBoolean(this, Preference.NAV_DRAWER_ALREADY_USED, true);
            toggleNavDrawerAfterTwoSeconds();
        }
    }

    private void toggleNavDrawerAfterTwoSeconds() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getDrawer().openDrawer(GravityCompat.START);
            }

        }, TWO_SECONDS);
    }

    private boolean wasExplainShakeDialogAlreadyDisplayed() {
        boolean alreadyDisplayed = FoomlaPreferences.getBoolean(this,
                Preference.RANDOM_TRAINING_ON_SHAKE_DIALOG_ALREADY_DISPLAYED);

        if (!alreadyDisplayed) {
            FoomlaPreferences.setBoolean(this, Preference.RANDOM_TRAINING_ON_SHAKE_DIALOG_ALREADY_DISPLAYED, true);
        }

        return alreadyDisplayed;
    }
}
