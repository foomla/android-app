package org.foomla.api.entities.twizard;

import java.io.Serializable;

import org.foomla.api.entities.base.EntityWithTitle;

public interface ExerciseProperty extends EntityWithTitle, Serializable {

    String getValue();

    void setValue(String value);

}
