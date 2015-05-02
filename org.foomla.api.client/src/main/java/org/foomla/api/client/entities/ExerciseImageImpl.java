package org.foomla.api.client.entities;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.foomla.api.entities.twizard.ExerciseImage;

import java.io.Serializable;

@JsonDeserialize(as = ExerciseImageImpl.class)
public class ExerciseImageImpl implements ExerciseImage, Serializable {

    private String title;

    private Integer id;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }
}
