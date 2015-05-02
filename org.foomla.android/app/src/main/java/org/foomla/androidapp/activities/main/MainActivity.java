package org.foomla.androidapp.activities.main;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.BaseActivityWithNavDrawer;
import org.foomla.androidapp.activities.edittraining.EditTrainingActivity;
import org.foomla.androidapp.activities.exercisedetail.ExerciseDetailIntent;
import org.foomla.androidapp.activities.mytrainings.MyTrainingsActivity;
import org.foomla.androidapp.activities.news.NewsUtil;
import org.foomla.androidapp.activities.user.UserActivity;
import org.foomla.androidapp.async.DownloadLatestExerciseTask;
import org.foomla.androidapp.async.DownloadNewsTask;
import org.foomla.androidapp.async.DownloadTask;
import org.foomla.androidapp.preferences.FoomlaPreferences;
import org.foomla.androidapp.preferences.FoomlaPreferences.Preference;
import org.foomla.androidapp.widgets.ExplainShakeDialog;
import org.foomla.api.client.FoomlaClient;
import org.foomla.api.entities.twizard.Exercise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.synd.SyndEntry;
import com.squareup.seismic.ShakeDetector;

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

        setContentView(R.layout.activity_main);
        createNavDrawer();

        mainFragment = new MainFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_main, mainFragment).commit();
    }

    @Override
    public void onLoadLatestExercise() {

        DownloadTask.DownloadHandler<Exercise> handler = new DownloadTask.DownloadHandler<Exercise>() {
            @Override
            public void handle(final Exercise exercise) {
                mainFragment.showLatestExercise(exercise);
            }
        };

        FoomlaClient foomlaClient = ((FoomlaApplication) getApplication()).getFoomlaClient();
        new DownloadLatestExerciseTask(this, handler) {
            @Override
            protected int getLoadingTextId() {

                // not needed
                return 0;
            }

            @Override
            protected void hideProgressbar() {
                mainFragment.hideProgressBar(R.id.latestExerciseProgressBar, R.id.latestExerciseContainer);
            }

            @Override
            protected void onCancelled() {
                hideProgressbar();
                showUserHint(R.string.downloadtask_alert_no_data_desc);
            }

            @Override
            protected void showProgressbar() {
                mainFragment.showProgressBar(R.id.latestExerciseProgressBar, R.id.latestExerciseContainer);
            }
        }.execute(foomlaClient);
    }

    @Override
    public void onLoadNewsFeed() {
        try {
            new DownloadNewsTask(this, new DownloadNewsTask.Handler() {

                @Override
                public void onFinish(final List<SyndEntry> feedEntries) {
                    if (!feedEntries.isEmpty()) {
                        mainFragment.showLatestNewsEntry(feedEntries.get(0));
                    }
                }
            }) {
                @Override
                protected void onPostExecute(final List<SyndEntry> syndEntries) {
                    super.onPostExecute(syndEntries);
                    mainFragment.hideProgressBar(R.id.latestNewsEntryProgressBar, R.id.latestNewsEntryContainer);
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    mainFragment.showProgressBar(R.id.latestNewsEntryProgressBar, R.id.latestNewsEntryContainer);
                }
            }.execute(new URL(getString(R.string.news_url)));

        } catch (MalformedURLException e) {
            LOGGER.error("Feed URL is not valid: {}", e.getMessage(), e);
        }
    }

    @Override
    public void onOpenWebsite() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.website_url))));
    }

    @Override
    public void onShowAccountActivity() {
        startActivity(new Intent(this, UserActivity.class));
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
    public void onShowNewsDetailActivity(final SyndEntry feedEntry) {
        startActivity(NewsUtil.createNewsDetailIntent(this, feedEntry));
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
                getNavDrawer().toggleNavDrawer();
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
