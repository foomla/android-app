package org.foomla.androidapp.async;

import java.util.List;

import org.foomla.api.client.FoomlaClient;
import org.foomla.api.client.providers.ExerciseRatingProvider;
import org.foomla.api.entities.twizard.ExerciseRating;

import android.app.Activity;

/**
 * Downloades all ratings for a specific exercise from foomla server using the
 * {@link org.foomla.api.client.providers.ExerciseRatingProvider} returned by {@link org.foomla.api.client.FoomlaClient}.
 */
public class DownloadExercisesRatingTask extends DownloadTask<List<ExerciseRating>> {

    public DownloadExercisesRatingTask(final Activity activity, final DownloadHandler<List<ExerciseRating>> handler) {
        super(activity, handler);
    }

    @Override
    protected List<ExerciseRating> download(final FoomlaClient foomlaClient) throws Exception {
        ExerciseRatingProvider exerciseRatingProvider = foomlaClient.getProvider(ExerciseRatingProvider.class);
        return exerciseRatingProvider.getAll();
    }
}
