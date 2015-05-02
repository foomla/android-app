package org.foomla.androidapp.service;

import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.entities.twizard.TrainingPhase;

import java.util.List;

public interface ExerciseService {

    List<Exercise> list();

    List<Exercise> filter(TrainingPhase trainingPhase);
}
