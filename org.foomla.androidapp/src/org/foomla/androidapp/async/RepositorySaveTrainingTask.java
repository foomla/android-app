package org.foomla.androidapp.async;

import org.foomla.androidapp.persistence.Repository;
import org.foomla.api.client.FoomlaClient;
import org.foomla.api.client.providers.TrainingProvider;
import org.foomla.api.entities.twizard.Training;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class RepositorySaveTrainingTask extends RepositoryLoadTask<Training> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositorySaveTrainingTask.class);

    private final Repository<Training> repository;
    private FoomlaClient foomlaClient;

    public RepositorySaveTrainingTask(final Context context, final LoadHandler<Training> handler,
            final Repository<Training> repository) {
        super(context, handler);
        this.repository = repository;
    }

    public RepositorySaveTrainingTask withFoomlaClient(final FoomlaClient foomlaClient) {
        this.foomlaClient = foomlaClient;
        return this;
    }

    /**
     * The name of this method does not give a hint what this method does: saving trainings.
     *
     * @param   training  the training that is going to be saved on disk and online if connection is avail.
     *
     * @return  the training that was persisted locally.
     *
     * @throws  Exception
     */
    @Override
    protected Training load(final Object training) throws Exception {
        Training trainingObj = training instanceof Training ? (Training) training : null;

        if (trainingObj != null) {
            saveOnline(trainingObj);
            return saveOffline(trainingObj);
        }

        return null;
    }

    private Training saveOffline(final Training trainingObj) {
        try {
            return repository.save(trainingObj);
        } catch (Exception e) {
            LOGGER.error("Training was not persisted on local disk", e);
        }

        return null;
    }

    private void saveOnline(final Training trainingObj) {
        try {
            if (foomlaClient != null && isOnline() && isUserLoggedIn()) {
                LOGGER.info("Save training online");
                foomlaClient.getProvider(TrainingProvider.class).saveTraining(trainingObj);
            }
        } catch (Exception e) {
            LOGGER.error("Training was not persisted online", e);
        }
    }

    /**
     * Determine whether user is online with internet connection or not.
     *
     * @return  true if connection is avail or false if not.
     */
    protected boolean isOnline() {
        ConnectivityManager manager = (ConnectivityManager) getContext().getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo[] allNetworkInfo = manager.getAllNetworkInfo();

        if (allNetworkInfo != null) {
            for (NetworkInfo networkInfo : allNetworkInfo) {
                if (networkInfo.isAvailable() && networkInfo.isConnected()) {
                    return true;
                }
            }
        }

        return true;
    }

    protected boolean isUserLoggedIn() {

        // TODO
        return true;
    }
}
