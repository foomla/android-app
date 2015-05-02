package org.foomla.api.client.entities;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.foomla.api.entities.User;
import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.entities.twizard.ExerciseRating;

@JsonDeserialize(as = ExerciseRatingImpl.class)
public class ExerciseRatingImpl implements ExerciseRating {

    private User user;

    private Integer id;
    private Integer value;

    private String userComment;

    private Exercise exercise;

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(final User user) {
        this.user = user;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(final Integer id) {
        this.id = id;
    }

    @Override
    public Exercise getExercise() {
        return exercise;
    }

    @Override
    public void setExercise(final Exercise exercise) {
        this.exercise = exercise;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public void setValue(final Integer value) {
        this.value = value;
    }

    @Override
    public String getUserComment() {
        return userComment;
    }

    @Override
    public void setUserComment(final String userComment) {
        this.userComment = userComment;
    }
}
