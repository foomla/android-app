package org.foomla.androidapp.data;

import java.io.Serializable;

public class Comment implements Serializable {
    private String email;
    private String value;

    public Comment() {}

    public Comment(String email, String value) {
        this.email = email;
        this.value = value;
    }

    public String getEmail() {
        return email;
    }

    public String getValue() {
        return value;
    }
}
