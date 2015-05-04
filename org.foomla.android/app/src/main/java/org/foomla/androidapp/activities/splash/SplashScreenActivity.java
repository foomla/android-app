package org.foomla.androidapp.activities.splash;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.R;
import org.foomla.androidapp.activities.BaseActivity;
import org.foomla.androidapp.activities.login.LoginInfoActivity;
import org.foomla.androidapp.activities.main.MainActivity;
import org.foomla.androidapp.preferences.FoomlaPreferences;
import org.foomla.androidapp.preferences.FoomlaPreferences.Preference;

import org.foomla.api.client.FoomlaClient;

import android.app.Activity;

import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import android.widget.TextView;

public class SplashScreenActivity extends BaseActivity {

    private static final long SPLASH_DISPLAY_TIME = 2000;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.foomla.androidapp.R.layout.activity_splash_screen);
        setAppVersionInBottomTextView();
        getSupportActionBar().hide();
        getFoomlaApplication().initLogging();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initClientAndStartMainActivity();
    }

    private void initClientAndStartMainActivity() {

        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(final Void... params) {
                return null;
            }

            @Override
            protected void onPostExecute(final Void result) {
                startActivityDelayed(MainActivity.class);
            }
        };
        asyncTask.execute();
    }

    private void setAppVersionInBottomTextView() {
        String textWithAppVersion = String.format(getString(R.string.splash_screen_app_version),
                ((FoomlaApplication) getApplication()).getVersionName());

        ((TextView) findViewById(R.id.splashScreenAppVersion)).setText(textWithAppVersion);
    }

    private void startActivityDelayed(final Class<? extends Activity> activityClass) {
        new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, activityClass));
                    finish();
                }
            }, SPLASH_DISPLAY_TIME);
    }
    
    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
