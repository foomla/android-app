package org.foomla.api.entities.twizard;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import org.foomla.api.entities.base.EntityWithTitle;

public interface Exercise extends EntityWithTitle, Serializable {

    void setVisibility(ExerciseVisibility exerciseVisibility);

    ExerciseVisibility getVisibility();

    List<AgeClass> getAgeClasses();

    String getAuxiliaryMaterial();

    Date getCreatedAt();

    ExerciseStatus getExerciseStatus();

    List<ExerciseImage> getImages();

    int getMaxPlayers();

    int getMinPlayers();

    String getNote();

    String getObjective();

    List<ExerciseProperty> getProperties();

    String getSchedule();

    String getSetting();

    TrainingFocus getTrainingFocus();

    List<TrainingPhase> getTrainingPhases();

    Date getUpdatedAt();

    void setAgeClasses(List<AgeClass> ageClasses);

    void setAuxiliaryMaterial(String auxiliaryMaterial);

    void setCreatedAt(Date createdAt);

    void setExerciseStatus(ExerciseStatus exerciseStatus);

    void setImages(List<ExerciseImage> exerciseImages);

    void setMaxPlayers(int maxPlayers);

    void setMinPlayers(int minPlayers);

    void setNote(String note);

    void setObjective(String objective);

    void setProperties(List<ExerciseProperty> property);

    void setSchedule(String schedule);

    void setSetting(String setting);

    void setTrainingFocus(TrainingFocus trainingFocus);

    void setTrainingPhases(List<TrainingPhase> trainingPhase);

    void setUpdatedAt(Date updatedAt);
}
