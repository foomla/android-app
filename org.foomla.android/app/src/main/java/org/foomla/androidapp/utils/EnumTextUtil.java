package org.foomla.androidapp.utils;

import android.content.Context;

import org.foomla.androidapp.R;
import org.foomla.androidapp.domain.AgeClass;
import org.foomla.androidapp.domain.TrainingFocus;
import org.foomla.androidapp.domain.TrainingPhase;

public class EnumTextUtil {

    public static String getText(Context context, AgeClass ageClass) {
        switch (ageClass) {
            case MINI:
                return context.getString(R.string.AGE_CLASS_MINI);
            case SENIOR:
                return context.getString(R.string.AGE_CLASS_SENIOR);
            case U11:
                return context.getString(R.string.AGE_CLASS_U11);
            case U13:
                return context.getString(R.string.AGE_CLASS_U13);
            case U15:
                return context.getString(R.string.AGE_CLASS_U15);
            case U17:
                return context.getString(R.string.AGE_CLASS_U17);
            case U19:
                return context.getString(R.string.AGE_CLASS_U19);
            case U9:
                return context.getString(R.string.AGE_CLASS_U9);
            default:
                return "";
        }
    }

    public static String getText(Context context, TrainingFocus trainingFocus) {
        switch (trainingFocus) {
            case ENDURANCE:
                return context.getString(R.string.TRAINING_FOCUS_ENDURANCE);
            case KNOWLEDGE:
                return context.getString(R.string.TRAINING_FOCUS_KNOWLEDGE);
            case MENTAL:
                return context.getString(R.string.TRAINING_FOCUS_MENTAL);
            case TACTIC:
                return context.getString(R.string.TRAINING_FOCUS_TACTIC);
            case TECHNIC:
                return context.getString(R.string.TRAINING_FOCUS_TECHNIC);
            default:
                return "";
        }
    }

    public static String getText(Context context, TrainingPhase trainingPhase) {
        switch (trainingPhase) {
            case ARRIVAL:
                return context.getString(R.string.TRAINING_PHASE_ARRIVAL);
            case WARM_UP:
                return context.getString(R.string.TRAINING_PHASE_WARM_UP);
            case MAIN:
                return context.getString(R.string.TRAINING_PHASE_MAIN);
            case SCRIMMAGE:
                return context.getString(R.string.TRAINING_PHASE_SCRIMMAGE);
            default:
                return "";
        }
    }

}
