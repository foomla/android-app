package org.foomla.androidapp;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings.Secure;

import org.foomla.androidapp.domain.User;
import org.foomla.androidapp.preferences.FoomlaPreferences;
import org.foomla.androidapp.preferences.FoomlaPreferences.Preference;
import org.foomla.androidapp.service.ExerciseService;
import org.foomla.androidapp.service.ExerciseServiceImpl;
import org.foomla.androidapp.service.TrainingService;
import org.foomla.androidapp.service.TrainingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FoomlaApplication extends Application {

    public static final User ANONYMOUS_USER = new User("ANONYMOUS");

    public static final String APP_PREFERENCES_KEY = "SHARED_PREFERENCES";

    public static final String FOOMLA_LOG_TAG = "[FOOMLA]";

    private static final Logger LOGGER = LoggerFactory.getLogger(FoomlaApplication.class);


    private ExerciseService exerciseService;
    private TrainingService trainingService;

    private User user = null;

    public ExerciseService getExerciseService() throws IOException {
        if (exerciseService == null) {
            exerciseService = new ExerciseServiceImpl(getResources());
        }

        return exerciseService;
    }

    public TrainingService getTrainingService() throws IOException {
        if (trainingService == null) {
            trainingService = new TrainingServiceImpl(getExerciseService());
        }

        return trainingService;
    }

    public String getOAuthAuthorizeUrl() {
        return getString(R.string.auth_url) + "?client_id=" + getString(R.string.client_id) + "&redirect_uri="
                + getString(R.string.redirect_url);
    }

    public String getOAuthRedirectUrl() {
        return getString(R.string.redirect_url);
    }

    public User getUser() {
        return this.user;
    }

    public String getVersionName() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            return "";
        }
    }

    public boolean isLoggedIn() {
        return false;
    }

}
