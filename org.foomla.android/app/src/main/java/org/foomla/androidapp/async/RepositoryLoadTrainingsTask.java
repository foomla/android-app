package org.foomla.androidapp.async;

import android.content.Context;

import org.foomla.androidapp.domain.Training;
import org.foomla.androidapp.persistence.Repository;

import java.util.List;

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
