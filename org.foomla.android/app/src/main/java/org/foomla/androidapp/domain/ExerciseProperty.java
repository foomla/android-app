package org.foomla.androidapp.domain;

import java.io.Serializable;


public class ExerciseProperty extends EntityWithTitle implements Serializable {

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
