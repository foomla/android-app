package org.foomla.androidapp.async;

import org.foomla.api.client.FoomlaClient;
import org.foomla.api.client.providers.ExerciseRatingProvider;
import org.foomla.api.entities.twizard.ExerciseRating;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import android.os.AsyncTask;

public class RateExerciseTask extends AsyncTask<ExerciseRating, Void, Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateExerciseTask.class);

    private final FoomlaClient foomlaClient;

    public RateExerciseTask(final Context context, final FoomlaClient foomlaClient) {
        this.foomlaClient = foomlaClient;
    }

    @Override
    protected Void doInBackground(final ExerciseRating... params) {
        ExerciseRatingProvider provider = foomlaClient.getProvider(ExerciseRatingProvider.class);
        try {
            provider.create(params[0]);
        } catch (Exception e) {
            LOGGER.error("Unable to load data!", e);
            cancel(true);
        }

        return null;
    }
}
