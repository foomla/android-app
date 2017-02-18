package org.foomla.androidapp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface Training extends EntityWithTitle, Serializable {

    void setTitle(String title);

    String getTitle();

    void setOwner(User user);

    User getOwner();

    void setDate(Date date);

    Date getDate();

    void setExercises(List<Exercise> exercises);

    List<Exercise> getExercises();

    boolean setExercise(int idx, Exercise exercise);

    Set<TrainingFocus> getTrainingFocus();

    void setComment(String comment);

    String getComment();
}
