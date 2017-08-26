package org.foomla.androidapp.service;

import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.domain.TrainingPhase;

import java.io.IOException;
import java.util.List;

public interface ExerciseService {

    interface Callback<T> {
        void onResult(T result);

        void onFailure();
    }

    void list(Callback<List<Exercise>> callback, boolean isPro) throws IOException;

    List<Exercise> filter(TrainingPhase trainingPhase);

    void random(Callback<Exercise> callback, boolean isPro) throws IOException;
}
