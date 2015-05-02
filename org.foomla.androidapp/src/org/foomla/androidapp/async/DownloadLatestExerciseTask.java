package org.foomla.androidapp.async;

import org.foomla.api.client.FoomlaClient;
import org.foomla.api.client.service.ExerciseServiceProvider;
import org.foomla.api.entities.twizard.Exercise;

import android.app.Activity;

/**
 * Downloades the latest Exercise from foomla server using the {@link ExerciseServiceProvider} returned by
 * {@link FoomlaClient}.
 */
public class DownloadLatestExerciseTask extends DownloadTask<Exercise> {

    public DownloadLatestExerciseTask(Activity activity, DownloadHandler<Exercise> handler) {
        super(activity, handler);
    }

    @Override
    protected Exercise download(FoomlaClient foomlaClient) throws Exception {
        ExerciseServiceProvider exerciseServiceProvider = foomlaClient.getProvider(ExerciseServiceProvider.class);
        return exerciseServiceProvider.getLatest();
    }

}
