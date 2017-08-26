package org.foomla.androidapp.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.activities.exercisebrowser.ExerciseFilter;

import static org.foomla.androidapp.preferences.FoomlaPreferences.Preference.EXERCISE_FILTER;

public class FoomlaPreferences {

    public enum Preference {

        LOGIN(true), NAV_DRAWER_ALREADY_USED(false), RANDOM_EXERCISE_ON_SHAKE_DIALOG_ALREADY_DISPLAYED(false), RANDOM_TRAINING_ON_SHAKE_DIALOG_ALREADY_DISPLAYED(
                false), REFRESH_TOKEN(null), EXERCISE_FILTER(null), DISMISS_GO_PRO_MAIN(false), DISMISS_GO_PRO_EXERCISES(false);

        public Object defaultValue;

        Preference(Object defaultValue) {
            this.defaultValue = defaultValue;
        }
    }

    public static ExerciseFilter getExerciseFilter(Context context) {
        String exerciseFilter = getString(context, EXERCISE_FILTER);
        if(exerciseFilter != null) {
            return new Gson().fromJson(exerciseFilter, ExerciseFilter.class);
        } else {
            return null;
        }
    }

    public static void setExerciseFilter(Context context, ExerciseFilter exerciseFilter) {
        String exerciseFilterJson = new Gson().toJson(exerciseFilter);
        setString(context, EXERCISE_FILTER, exerciseFilterJson);
    }

    public static void clearExerciseFilter(Context context) {
        SharedPreferences sharedPreference = getSharedPreference(context);
        Editor editor = sharedPreference.edit();
        editor.remove(EXERCISE_FILTER.name());
        editor.commit();
    }

    public static boolean getBoolean(Context context, Preference preference) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        return sharedPreferences.getBoolean(preference.name(), (Boolean) preference.defaultValue);
    }

    public static String getString(Context context, Preference preference) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        return sharedPreferences.getString(preference.name(), (String) preference.defaultValue);
    }

    public static void setBoolean(Context context, Preference preference, Boolean value) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(preference.name(), value);
        editor.commit();
    }

    public static void setString(Context context, Preference preference, String value) {
        SharedPreferences sharedPreferences = getSharedPreference(context);
        Editor editor = sharedPreferences.edit();
        editor.putString(preference.name(), value);
        editor.commit();
    }

    private static SharedPreferences getSharedPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FoomlaApplication.APP_PREFERENCES_KEY,
                Context.MODE_PRIVATE);
        return sharedPreferences;
    }
}
