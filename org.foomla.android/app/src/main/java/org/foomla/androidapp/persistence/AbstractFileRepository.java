package org.foomla.androidapp.persistence;


import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public abstract class AbstractFileRepository<T extends Serializable> implements Repository<T> {

    private Gson gson = new Gson();

    protected Object convert(File file, Class target) throws IOException {
        return gson.fromJson(new FileReader(file), target);
    }

    protected void write(File destination, T entity) throws IOException {
        gson.toJson(entity, new FileWriter(destination));
    }
}
