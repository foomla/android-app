package org.foomla.androidapp.async;

import java.util.List;

import org.foomla.api.client.FoomlaClient;
import org.foomla.api.client.service.ExerciseServiceProvider;
import org.foomla.api.entities.twizard.Exercise;

import android.app.Activity;

/**
 * Downloades all Exercises from foomla server using the {@link ExerciseServiceProvider} returned by
 * {@link FoomlaClient}.
 */
public class DownloadExercisesTask extends DownloadTask<List<Exercise>> {

    public DownloadExercisesTask(Activity activity, DownloadHandler<List<Exercise>> handler) {
        super(activity, handler);
    }

    @Override
    protected List<Exercise> download(FoomlaClient foomlaClient) throws Exception {
        ExerciseServiceProvider exerciseServiceProvider = foomlaClient.getProvider(ExerciseServiceProvider.class);
        return exerciseServiceProvider.getAll();
    }
}
