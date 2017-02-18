package org.foomla.androidapp.domain;

import java.io.Serializable;


public interface ExerciseProperty extends EntityWithTitle, Serializable {

    String getValue();

    void setValue(String value);

}
