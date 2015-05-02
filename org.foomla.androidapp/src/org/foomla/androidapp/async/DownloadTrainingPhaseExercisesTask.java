package org.foomla.androidapp.async;

import java.util.List;

import org.foomla.api.client.FoomlaClient;
import org.foomla.api.client.service.ExerciseServiceProvider;
import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.entities.twizard.TrainingPhase;

import android.app.Activity;

/**
 * Downloades all Exercises from foomla server using the {@link ExerciseServiceProvider} returned by
 * {@link org.foomla.api.client.FoomlaClient}.
 */
public class DownloadTrainingPhaseExercisesTask extends DownloadTask<List<Exercise>> {

    private final int trainingPhase;

    public DownloadTrainingPhaseExercisesTask(Activity activity, DownloadHandler<List<Exercise>> handler,
            int trainingPhase) {
        super(activity, handler);
        this.trainingPhase = trainingPhase;
    }

    @Override
    protected List<Exercise> download(FoomlaClient foomlaClient) throws Exception {
        ExerciseServiceProvider exerciseServiceProvider = foomlaClient.getProvider(ExerciseServiceProvider.class);
        return exerciseServiceProvider.getAll(TrainingPhase.getById(trainingPhase).name());
    }
}
