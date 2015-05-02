package org.foomla.androidapp.data;

import java.io.Serializable;

public class ExerciseRatingResult implements Serializable {

    private int id;
    private int exerciseId;
    private int value;

    public ExerciseRatingResult(int id, int exerciseId, int value) {
        this.id = id;
        this.exerciseId = exerciseId;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public int getValue() {
        return value;
    }
}
