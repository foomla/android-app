package org.foomla.api.client.service;

import java.util.ArrayList;
import java.util.List;

import org.foomla.api.client.FoomlaClient.RestConnectionSettings;
import org.foomla.api.entities.twizard.Training;
import org.foomla.api.services.twizard.RandomTrainingService;
import org.foomla.api.services.twizard.TrainingService;

public class TrainingServiceProvider extends ServiceProvider implements RandomTrainingService, TrainingService {

    public class TrainingList extends ArrayList<Training> {

    }

    public TrainingServiceProvider(RestConnectionSettings settings) {
        super(settings);
    }

    @Override
    public Training create(Training training) {
        return url("/trainings").post(training).as(Training.class);
    }

    @Override
    public List<Training> getAll() {
        return url("/trainings").get().as(TrainingList.class);
    }

    @Override
    public Training getById(int id) {
        return url("/trainings/{}", id).get().as(Training.class);
    }

    @Override
    public Training getRandom() {
        return url("/training/random").get().as(Training.class);
    }

    @Override
    public void remove(int id) {
        url("/trainings/{}", id).delete();
    }

    @Override
    public void update(int id, Training training) {
        url("/trainings/{}", id).put(training);
    }

}
