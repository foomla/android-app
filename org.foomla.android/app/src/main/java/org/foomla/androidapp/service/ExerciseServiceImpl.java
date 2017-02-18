package org.foomla.androidapp.service;

import android.content.res.Resources;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import org.foomla.androidapp.R;
import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.domain.TrainingPhase;
import org.foomla.androidapp.utils.JsonResourceReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExerciseServiceImpl implements ExerciseService {

    private final Random random = new Random();

    private final List<Exercise> exercises = new ArrayList<>();

    public ExerciseServiceImpl(final Resources resources) throws IOException {
        final ExerciseImpl[] exerciseArr =
                new JsonResourceReader(resources, R.raw.exercises).read(ExerciseImpl[].class);

        for (ExerciseImpl exercise : exerciseArr) {
            exercises.add(exercise);
        }
    }

    @Override
    public List<Exercise> list() {
        return new ArrayList<>(exercises);
    }

    @Override
    public List<Exercise> filter(final TrainingPhase trainingPhase) {
        return Lists.newArrayList(Collections2.filter(exercises, new Predicate<Exercise>() {
            @Override
            public boolean apply(Exercise input) {
                return input.getTrainingPhases().contains(trainingPhase);
            }
        }));
    }

    @Override
    public Exercise random() {
        int randomIndex = random.nextInt(exercises.size() - 1);
        return exercises.get(randomIndex);
    }
}
