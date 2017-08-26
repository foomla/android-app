package org.foomla.androidapp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class Exercise extends EntityWithTitle {

    private ExerciseVisibility exerciseVisibility;

    private List<AgeClass> ageClasses;

    private String auxiliaryMaterial;

    private Date createdAt;

    private ExerciseStatus exerciseStatus;

    private List<String> images;

    private int minPlayers;

    private String note;

    private String objective;

    private List<ExerciseProperty> properties;

    private String schedule;

    private String setting;

    private TrainingFocus trainingFocus;

    private List<TrainingPhase> trainingPhases;

    private Date updatedAt;

    public ExerciseVisibility getExerciseVisibility() {
        return exerciseVisibility;
    }

    public void setExerciseVisibility(ExerciseVisibility exerciseVisibility) {
        this.exerciseVisibility = exerciseVisibility;
    }

    public List<AgeClass> getAgeClasses() {
        return ageClasses;
    }

    public void setAgeClasses(List<AgeClass> ageClasses) {
        this.ageClasses = ageClasses;
    }

    public String getAuxiliaryMaterial() {
        return auxiliaryMaterial;
    }

    public void setAuxiliaryMaterial(String auxiliaryMaterial) {
        this.auxiliaryMaterial = auxiliaryMaterial;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ExerciseStatus getExerciseStatus() {
        return exerciseStatus;
    }

    public void setExerciseStatus(ExerciseStatus exerciseStatus) {
        this.exerciseStatus = exerciseStatus;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public List<ExerciseProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ExerciseProperty> properties) {
        this.properties = properties;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getSetting() {
        return setting;
    }

    public void setSetting(String setting) {
        this.setting = setting;
    }

    public TrainingFocus getTrainingFocus() {
        return trainingFocus;
    }

    public void setTrainingFocus(TrainingFocus trainingFocus) {
        this.trainingFocus = trainingFocus;
    }

    public List<TrainingPhase> getTrainingPhases() {
        return trainingPhases;
    }

    public void setTrainingPhases(List<TrainingPhase> trainingPhases) {
        this.trainingPhases = trainingPhases;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
