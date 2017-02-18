package org.foomla.androidapp.persistence;

import android.content.Context;

import org.foomla.androidapp.FoomlaApplication;
import org.foomla.androidapp.data.UserMode;
import org.foomla.androidapp.domain.Training;
import org.foomla.androidapp.exception.FoomlaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TrainingProxyRepository implements Repository<Training> {

    private static TrainingProxyRepository instance;

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingProxyRepository.class);

    private final FoomlaApplication foomlaApplication;
    private final TrainingFileRepository fileRepositoryAuthorized;
    private final TrainingFileRepository fileRepositoryUnauthorized;

    private TrainingProxyRepository(final FoomlaApplication application, final Context context) {
        this.foomlaApplication = application;
        this.fileRepositoryUnauthorized = TrainingFileRepository.getInstance(context, UserMode.UNAUTHORIZED);
        this.fileRepositoryAuthorized = TrainingFileRepository.getInstance(context, UserMode.AUTHORIZED);
    }

    public static TrainingProxyRepository getInstance(final FoomlaApplication application, final Context context) {
        if (instance == null) {
            instance = new TrainingProxyRepository(application, context);
        }

        return instance;
    }

    @Override
    public Training save(final Training entity) {
        if (foomlaApplication.isLoggedIn()) {
            LOGGER.info("Save training on remote server and on device for authorized user");
            return saveTrainingOnServerAndDevice(entity);
        } else {
            LOGGER.info("Save training only on device for unauthorized user.");
            return saveTrainingOnDevice(entity);
        }
    }

    @Override
    public List<Training> getAll() {
        if (foomlaApplication.isLoggedIn()) {
            return fileRepositoryAuthorized.getAll();
        } else {
            return fileRepositoryUnauthorized.getAll();
        }
    }

    @Override
    public Training getById(final int id) {
        if (foomlaApplication.isLoggedIn()) {
            return fileRepositoryAuthorized.getById(id);
        } else {
            return fileRepositoryUnauthorized.getById(id);
        }
    }

    @Override
    public void delete(final int id) {
        if (foomlaApplication.isLoggedIn()) {
            deleteTrainingOnServerAndDevice(id);
        } else {
            fileRepositoryUnauthorized.delete(id);
        }
    }

    private Training saveTrainingOnServerAndDevice(final Training entity) {
        final Training saved = remoteRepository.save(entity);
        if (saved != null) {
            LOGGER.info("Saved training on remote server with ID: {}", saved.getId());
            try {
                saveRemoteTrainingOnDevice(saved);
            } catch (FoomlaException e) {
                LOGGER.warn("Failed to save training on local device", e);
            }
        }

        return saved;
    }

    private void saveRemoteTrainingOnDevice(final Training remoteTraining) {
        if (remoteTraining.getId() != null && remoteTraining.getId() > 0) {
            Training localTraining = fileRepositoryAuthorized.save(remoteTraining);

            if (localTraining == null || localTraining.getId() <= 0) {
                LOGGER.warn("Failed to save training on device!");
            }
        } else {
            LOGGER.warn("Remote training has no or incorrect ID set: {}", remoteTraining.getId());
        }
    }

    private Training saveTrainingOnDevice(final Training training) {
        return fileRepositoryUnauthorized.save(training);
    }

    private void deleteTrainingOnServerAndDevice(final int id) {
        remoteRepository.delete(id);
        LOGGER.info("Deleted training on remote server with ID: {}", id);

        try {
            fileRepositoryAuthorized.delete(id);
        } catch (FoomlaException e) {
            LOGGER.warn("Failed to delete training from local device", e);
        }
    }
}
