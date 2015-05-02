package org.foomla.androidapp.service;

import android.content.res.Resources;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import org.foomla.androidapp.R;
import org.foomla.androidapp.utils.JsonResourceReader;
import org.foomla.api.client.entities.ExerciseImpl;
import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.entities.twizard.TrainingPhase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExerciseServiceImpl implements ExerciseService {

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
}
