package org.foomla.api.client.service;

import org.foomla.api.client.FoomlaClient.RestConnectionSettings;
import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.services.twizard.ExerciseList;
import org.foomla.api.services.twizard.ExerciseListService;
import org.foomla.api.services.twizard.ExerciseService;

public class ExerciseServiceProvider extends ServiceProvider implements ExerciseListService, ExerciseService {

    public ExerciseServiceProvider(RestConnectionSettings settings) {
        super(settings);
    }

    @Override
    public void create(Exercise exercise) {
        url("/exercises").post(exercise);
    }

    public ExerciseList getAll() {
        return url("/exercises").get().as(ExerciseList.class);
    }

    @Override
    public ExerciseList getAll(String phase) {
        return url("/exercises").queryParameter(ExerciseListService.ATTRIBUTE_TRAININGPHASE, phase)
                .get()
                .as(ExerciseList.class);
    }

    @Override
    public Exercise getById(int id) {
        return url("/exercise/{}", id).get().as(Exercise.class);
    }

    @Override
    public Exercise getLatest() {
        return url("/exercises/latest").get().as(Exercise.class);
    }

    @Override
    public void remove(int id) {
        url("/exercise/{}", id).delete();
    }

    @Override
    public void update(Exercise exercise) {
        url("/exercise").put(exercise);
    }

}
