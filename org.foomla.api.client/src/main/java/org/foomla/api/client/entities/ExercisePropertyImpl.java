package org.foomla.api.client.entities;

import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.foomla.api.entities.twizard.ExerciseProperty;

@JsonDeserialize(as = ExercisePropertyImpl.class)
public class ExercisePropertyImpl implements ExerciseProperty {

    private Integer id;

    private String title;
    private String value;

    public ExercisePropertyImpl() {
    }

    public ExercisePropertyImpl(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }
}
