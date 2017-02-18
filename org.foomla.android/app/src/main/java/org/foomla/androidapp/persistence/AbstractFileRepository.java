package org.foomla.androidapp.persistence;


import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;

public abstract class AbstractFileRepository<T extends Serializable> implements Repository<T> {

    private Gson gson = new Gson();

    protected Object convert(File file, Class target) throws IOException {
        String s = IOUtils.toString(new InputStreamReader(new FileInputStream(file)));
        return gson.fromJson(s, target);
    }

    protected void write(File destination, T entity) throws IOException {
        final String jsonString = gson.toJson(entity);
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(destination, false));
        out.write(jsonString);
        out.close();
    }
}
