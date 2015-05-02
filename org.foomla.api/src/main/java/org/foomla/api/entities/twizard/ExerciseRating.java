package org.foomla.api.entities.twizard;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.foomla.api.entities.User;
import org.foomla.api.entities.base.Entity;

public interface ExerciseRating extends Entity, Serializable {

    User getUser();

    void setUser(User user);

    @JsonIgnore
    Exercise getExercise();

    void setExercise(Exercise exercise);

    Integer getValue();

    void setValue(Integer value);

    String getUserComment();

    void setUserComment(String userComment);
}
