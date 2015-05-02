package org.foomla.androidapp.data;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractEntityBuilder<T> implements EntityBuilder<T> {

    private List<T> results;

    public AbstractEntityBuilder() {
        this.results = new ArrayList<T>();
    }

    protected void add(T result) {
        results.add(result);
    }

    public List<T> build() {
        return results;
    }
}
