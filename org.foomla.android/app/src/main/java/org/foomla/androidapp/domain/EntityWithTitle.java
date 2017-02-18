package org.foomla.androidapp.domain;

public abstract class EntityWithTitle extends Entity {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
