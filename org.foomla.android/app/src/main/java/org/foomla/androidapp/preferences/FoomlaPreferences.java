package org.foomla.androidapp.preferences;

import org.foomla.androidapp.FoomlaApplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class FoomlaPreferences {

    public enum Preference {

        LOGIN(true), NAV_DRAWER_ALREADY_USED(false), RANDOM_EXERCISE_ON_SHAKE_DIALOG_ALREADY_DISPLAYED(false), RANDOM_TRAINING_ON_SHAKE_DIALOG_ALREADY_DISPLAYED(
                false), REFRESH_TOKEN(null);

        public Object defaultValue;

        Preference(Object defaultValue) {
            this.defaultValue = defaultValue;
        }
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
