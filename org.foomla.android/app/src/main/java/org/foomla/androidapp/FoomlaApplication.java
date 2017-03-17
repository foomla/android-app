package org.foomla.androidapp;

import android.app.Application;
import android.content.pm.PackageManager.NameNotFoundException;

import org.foomla.androidapp.domain.User;
import org.foomla.androidapp.service.ExerciseService;
import org.foomla.androidapp.service.ExerciseServiceImpl;
import org.foomla.androidapp.service.TrainingService;
import org.foomla.androidapp.service.TrainingServiceImpl;

import java.io.IOException;

public class FoomlaApplication extends Application {

    public static final String APP_PREFERENCES_KEY = "SHARED_PREFERENCES";
    public static final String PACKAGE_NAME_PRO = "org.foomla.androidapp.pro";
    public static final int MAX_TRAININGS_ON_FREE = 3;

    private ExerciseService exerciseService;
    private TrainingService trainingService;

    private User user = null;

    public boolean isProVersion() {
        return getPackageName().equals(PACKAGE_NAME_PRO);
    }

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
