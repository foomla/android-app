package org.foomla.androidapp.service;

import org.foomla.androidapp.domain.Exercise;
import org.foomla.androidapp.domain.TrainingPhase;

import java.util.List;

public interface ExerciseService {

    List<Exercise> list();

    List<Exercise> filter(TrainingPhase trainingPhase);

    Exercise random();
}
