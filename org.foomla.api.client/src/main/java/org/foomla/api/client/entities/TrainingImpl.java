package org.foomla.api.client.entities;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import org.foomla.api.entities.User;
import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.entities.twizard.Training;
import org.foomla.api.entities.twizard.TrainingFocus;

@JsonDeserialize(as = TrainingImpl.class)
public class TrainingImpl implements Training, Serializable {

    private Date date;

    @JsonDeserialize(contentAs = ExerciseImpl.class)
    private List<Exercise> exercises = new ArrayList<Exercise>();

    private Integer id;

    @JsonDeserialize(as = UserImpl.class)
    private User owner;

    private String title;

    private String comment;

    public TrainingImpl() { }

    public TrainingImpl(final String title, final Date date) {
        this.title = title;
        this.date = date;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public List<Exercise> getExercises() {
        return Collections.unmodifiableList(exercises);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public User getOwner() {
        return owner;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @JsonIgnore
    @Override
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

    @Override
    public void setDate(final Date date) {
        this.date = date;
    }

    @Override
    public boolean setExercise(final int idx, final Exercise exercise) {
        if (idx <= exercises.size()) {
            exercises.set(idx, exercise);
            return true;
        }

        return false;
    }

    @Override
    public void setExercises(final List<Exercise> exercises) {
        this.exercises = exercises;
    }

    @Override
    public void setId(final Integer id) {
        this.id = id;
    }

    @Override
    public void setOwner(final User user) {
        this.owner = user;
    }

    @Override
    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public void setComment(final String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Training[" + getTitle() + "," + getDate() + "]";
    }
}
