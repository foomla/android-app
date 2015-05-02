package org.foomla.androidapp.async;

import org.foomla.androidapp.R;
import org.foomla.api.client.FoomlaClient;
import org.foomla.api.client.service.TrainingServiceProvider;
import org.foomla.api.entities.twizard.Training;

import android.app.Activity;

public class DownloadRandomTrainingTask extends DownloadTask<Training> {

    public DownloadRandomTrainingTask(Activity activity, DownloadHandler<Training> handler) {
        super(activity, handler);
    }

    @Override
    protected Training download(FoomlaClient foomlaClient) throws Exception {
        TrainingServiceProvider trainingServiceProvider = foomlaClient.getProvider(TrainingServiceProvider.class);
        return trainingServiceProvider.getRandom();
    }

    @Override
    protected int getLoadingTextId() {
        return R.string.exercises;
    }
}
