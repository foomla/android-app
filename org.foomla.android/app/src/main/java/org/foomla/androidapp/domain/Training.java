package org.foomla.androidapp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Training extends EntityWithTitle implements Serializable {

    private User owner;

    private Date date;

    private List<Exercise> exercises;

    private String comment;


    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean setExercise(int idx, Exercise exercise) {
        if (idx <= exercises.size()) {
            exercises.set(idx, exercise);
            return true;
        }

        return false;
    }

    public Set<TrainingFocus> getTrainingFocus() {
        SortedSet<TrainingFocus> trainingFocusSet = new TreeSet<TrainingFocus>();
        for (Exercise exercise : getExercises()) {
            TrainingFocus trainingFocus = exercise.getTrainingFocus();
            if (trainingFocus != null) {
                trainingFocusSet.add(trainingFocus);
            }
        }

        return trainingFocusSet;
    }


}
