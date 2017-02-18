package org.foomla.androidapp.service;

import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.domain.Training;
import org.foomla.androidapp.domain.TrainingPhase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TrainingServiceImpl implements TrainingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingServiceImpl.class);

    private final Random intGenerator = new Random();

    private final ExerciseService exerciseService;

    public TrainingServiceImpl(final ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @Override
    public Training random() {
        final List<Exercise> phases = new ArrayList<>(5);

        addRandomExercise(phases, TrainingPhase.ARRIVAL);
        addRandomExercise(phases, TrainingPhase.WARM_UP);
        addRandomExercise(phases, TrainingPhase.MAIN);
        addRandomExercise(phases, TrainingPhase.MAIN);
        addRandomExercise(phases, TrainingPhase.SCRIMMAGE);

        Training t = new Training();
        t.setDate(new Date());
        t.setExercises(phases);

        return t;
    }

    private void addRandomExercise(final List<Exercise> phases, final TrainingPhase trainingPhase) {
        Exercise randomExercise = getRandomExercise(phases, trainingPhase, 10);
        if (randomExercise != null) {
            phases.add(randomExercise);
        } else {
            LOGGER.warn(String.format("Missing exercise for phase '%s'.", trainingPhase.name()));
        }
    }

    private Exercise getRandomExercise(final List<Exercise> phases, final TrainingPhase trainingPhase,
                                       final int maxTries) {
        Exercise exercise = getRandomExercise(trainingPhase);
        if (!phases.contains(exercise) || maxTries <= 0) {
            return exercise;
        }

        return getRandomExercise(phases, trainingPhase, maxTries - 1);
    }

    private Exercise getRandomExercise(final TrainingPhase trainingPhase) {
        List<Exercise> exerciseList = exerciseService.filter(trainingPhase);
        if (!exerciseList.isEmpty()) {
            return exerciseList.get(intGenerator.nextInt(exerciseList.size()));
        } else {
            return null;
        }
    }
}
