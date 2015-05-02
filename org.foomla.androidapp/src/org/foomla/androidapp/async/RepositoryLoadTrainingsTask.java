package org.foomla.androidapp.async;

import java.util.List;

import org.foomla.androidapp.persistence.Repository;
import org.foomla.api.entities.twizard.Training;

import android.content.Context;

public class RepositoryLoadTrainingsTask extends RepositoryLoadTask<List<Training>> {

    private final Repository<Training> repository;

    public RepositoryLoadTrainingsTask(Context context, LoadHandler<List<Training>> handler,
                                       Repository<Training> repository) {
        super(context, handler);
        this.repository = repository;
    }

    @Override
    protected List<Training> load(Object params) throws Exception {
        return repository.getAll();
    }
}
