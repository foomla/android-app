package org.foomla.androidapp.async;

import android.content.Context;

import org.foomla.androidapp.domain.Training;
import org.foomla.androidapp.persistence.Repository;

public class RepositoryLoadTrainingTask extends RepositoryLoadTask<Training> {

    private final Repository<Training> repository;

    public RepositoryLoadTrainingTask(Context context, RepositoryLoadTask.LoadHandler<Training> handler, Repository<Training> repository) {
        super(context, handler);
        this.repository = repository;
    }

    @Override
    protected Training load(Object id) throws Exception {
        int trainingId = Integer.valueOf((String) id);
        return repository.getById(trainingId);
    }
}
