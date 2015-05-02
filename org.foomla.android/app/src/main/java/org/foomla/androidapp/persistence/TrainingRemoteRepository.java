package org.foomla.androidapp.persistence;

import java.util.List;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.exception.FoomlaException;

import org.foomla.api.client.service.TrainingServiceProvider;
import org.foomla.api.entities.twizard.Training;

import android.content.Context;

public class TrainingRemoteRepository implements Repository<Training> {

    private static TrainingRemoteRepository instance;

    private FoomlaApplication application;
    private Context context;

    private TrainingRemoteRepository(final FoomlaApplication application, final Context context) {
        this.application = application;
        this.context = context;
    }

    public static TrainingRemoteRepository getInstance(final FoomlaApplication application, final Context context) {
        if (instance == null) {
            instance = new TrainingRemoteRepository(application, context);
        }

        return instance;
    }

    @Override
    public Training save(final Training entity) {
        try {
            return getProvider().create(entity);
        } catch (Exception e) {
            throw new FoomlaException("Error occurred while saving training on server", e);
        }
    }

    @Override
    public List<Training> getAll() {
        try {
            return getProvider().getAll();
        } catch (Exception e) {
            throw new FoomlaException("Error occurred while getting trainings from server", e);
        }
    }

    @Override
    public Training getById(final int id) {
        try {
            return getProvider().getById(id);
        } catch (Exception e) {
            throw new FoomlaException("Error occured while getting training '" + id + "' from server", e);
        }
    }

    @Override
    public void delete(final int id) {
        try {
            getProvider().remove(id);
        } catch (Exception e) {
            throw new FoomlaException("Error occurred while deleting training on server", e);
        }
    }

    private TrainingServiceProvider getProvider() {
        return application.getFoomlaClient().getProvider(TrainingServiceProvider.class);
    }
}
