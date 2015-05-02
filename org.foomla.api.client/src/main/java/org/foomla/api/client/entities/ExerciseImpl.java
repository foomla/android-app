package org.foomla.api.client.entities;

import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import org.foomla.api.entities.twizard.AgeClass;
import org.foomla.api.entities.twizard.Exercise;
import org.foomla.api.entities.twizard.ExerciseImage;
import org.foomla.api.entities.twizard.ExerciseProperty;
import org.foomla.api.entities.twizard.ExerciseStatus;
import org.foomla.api.entities.twizard.ExerciseVisibility;
import org.foomla.api.entities.twizard.TrainingFocus;
import org.foomla.api.entities.twizard.TrainingPhase;

@JsonDeserialize(as = ExerciseImpl.class)
public class ExerciseImpl implements Exercise {

    private List<AgeClass> ageClasses;

    private String auxiliaryMaterial;

    private Date createdAt;

    private ExerciseStatus exerciseStatus;

    private Integer id;

    private List<ExerciseImage> images;

    private int maxPlayers;

    private int minPlayers;

    private String note;

    private String objective;

    private List<ExerciseProperty> properties;

    private String schedule;

    private String setting;

    private String title;

    private TrainingFocus trainingFocus;

    private List<TrainingPhase> trainingPhases;

    private Date updatedAt;

    private ExerciseVisibility visibility;

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof ExerciseImpl)) {
            return false;
        }

        ExerciseImpl other = (ExerciseImpl) o;
        if (other.getId() != null && getId() != null) {

            // Dirty hack to speed up performance while comparing exercises
            return other.getId().intValue() == getId().intValue();
        } else {
            return false;
        }
    }

    @Override
    public List<AgeClass> getAgeClasses() {
        return this.ageClasses;
    }

    @Override
    public String getAuxiliaryMaterial() {
        return auxiliaryMaterial;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public ExerciseStatus getExerciseStatus() {
        return exerciseStatus;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public List<ExerciseImage> getImages() {
        return images;
    }

    @Override
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public int getMinPlayers() {
        return minPlayers;
    }

    @Override
    public String getNote() {
        return note;
    }

    @Override
    public String getObjective() {
        return objective;
    }

    @Override
    public List<ExerciseProperty> getProperties() {
        return properties;
    }

    @Override
    public String getSchedule() {
        return schedule;
    }

    @Override
    public String getSetting() {
        return setting;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public TrainingFocus getTrainingFocus() {
        return trainingFocus;
    }

    @Override
    public List<TrainingPhase> getTrainingPhases() {
        return trainingPhases;
    }

    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setAgeClasses(final List<AgeClass> ageClasses) {
        this.ageClasses = ageClasses;
    }

    @Override
    public void setAuxiliaryMaterial(final String auxiliaryMaterial) {
        this.auxiliaryMaterial = auxiliaryMaterial;
    }

    @Override
    public void setCreatedAt(final Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public void setExerciseStatus(final ExerciseStatus exerciseStatus) {
        this.exerciseStatus = exerciseStatus;
    }

    @Override
    public void setId(final Integer id) {
        this.id = id;
    }

    @Override
    public void setImages(final List<ExerciseImage> images) {
        this.images = images;
    }

    @Override
    public void setMaxPlayers(final int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    @Override
    public void setMinPlayers(final int minPlayers) {
        this.minPlayers = minPlayers;
    }

    @Override
    public void setNote(final String note) {
        this.note = note;
    }

    @Override
    public void setObjective(final String objective) {
        this.objective = objective;
    }

    @Override
    public void setProperties(final List<ExerciseProperty> properties) {
        this.properties = properties;
    }

    @Override
    public void setSchedule(final String schedule) {
        this.schedule = schedule;
    }

    @Override
    public void setSetting(final String setting) {
        this.setting = setting;
    }

    @Override
    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public void setTrainingFocus(final TrainingFocus trainingFocus) {
        this.trainingFocus = trainingFocus;
    }

    @Override
    public void setTrainingPhases(final List<TrainingPhase> trainingPhases) {
        this.trainingPhases = trainingPhases;
    }

    @Override
    public void setUpdatedAt(final Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public void setVisibility(final ExerciseVisibility visibility) {
        this.visibility = visibility;
    }

    @Override
    public ExerciseVisibility getVisibility() {
        return visibility;
    }
}
