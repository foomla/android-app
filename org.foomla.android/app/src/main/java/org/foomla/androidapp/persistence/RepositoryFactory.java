package org.foomla.androidapp.persistence;

import android.content.Context;

import org.foomla.androidapp.exception.FoomlaException;
import org.foomla.api.entities.base.Entity;

public class RepositoryFactory {

    private Context context;

    public RepositoryFactory(Context context) {
        this.context = context;
    }

    public Repository<? extends Entity> createExerciceRepository() {
        return ExerciseFileRepository.getInstance(context);
    }

    public Repository<? extends Entity> createTrainingRepository() {
        throw new FoomlaException("Not implemented");
    }
}
