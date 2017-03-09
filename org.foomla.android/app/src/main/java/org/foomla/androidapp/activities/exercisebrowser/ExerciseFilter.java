package org.foomla.androidapp.activities.exercisebrowser;


import org.foomla.androidapp.domain.AgeClass;
import org.foomla.androidapp.domain.TrainingFocus;

import java.util.ArrayList;
import java.util.List;

public class ExerciseFilter {

    private List<TrainingFocus> focuses = new ArrayList<>(TrainingFocus.values().length);
    private List<AgeClass> ageClasses = new ArrayList<>(AgeClass.values().length);

    public List<TrainingFocus> getFocuses() {
        return focuses;
    }

    public List<AgeClass> getAgeClasses() {
        return ageClasses;
    }

    public boolean shouldFilter() {
        return !getFocuses().isEmpty() || !getAgeClasses().isEmpty();
    }
}


