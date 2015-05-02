package org.foomla.androidapp.activities.trainingdetail;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.exception.FoomlaException;
import org.foomla.androidapp.persistence.TrainingProxyRepository;

import org.foomla.api.entities.twizard.Training;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;

public class TrainingDetailIntent extends Intent {

    public static final String EXTRA_TRAINING = "extra.trainingdetail.training";

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingDetailIntent.class);

    public TrainingDetailIntent(final Intent o) {
        super(o);
    }

    public TrainingDetailIntent(final Context context, final int trainingId) {
        super(context, TrainingDetailActivity.class);
        putExtra(EXTRA_TRAINING, trainingId);
    }

    public Training getTraining(final FoomlaApplication application, final Context context) {
        int trainingId = getIntExtra(EXTRA_TRAINING, -1);
        if (trainingId < 0) {
            return null;
        }

        try {
            TrainingProxyRepository repository = TrainingProxyRepository.getInstance(application, context);
            return repository.getById(trainingId);
        } catch (FoomlaException e) {
            LOGGER.error("Unable to load training by id: " + trainingId, e);
        }

        return null;
    }
}
