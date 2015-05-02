package org.foomla.androidapp.activities.edittraining;

import android.content.Intent;

public class EditTrainingIntent extends Intent {

    public static final String EXTRA_TRAINING_ID = "extra.training.id";

    public EditTrainingIntent(Intent o) {
        super(o);
    }

    public Integer getTrainingId() {
        int trainingId = getIntExtra(EXTRA_TRAINING_ID, -1);
        return trainingId > 0 ? Integer.valueOf(trainingId) : null;
    }
}
