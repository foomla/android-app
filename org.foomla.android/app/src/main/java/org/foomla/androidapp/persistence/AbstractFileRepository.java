package org.foomla.androidapp.persistence;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;

public abstract class AbstractFileRepository<T extends Serializable> implements Repository<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFileRepository.class);

    private Gson gson = new Gson();

    protected Object convert(File file, Class target) throws IOException {
        LOGGER.info("Deserializing file {}.", file.getPath());
        String s = IOUtils.toString(new InputStreamReader(new FileInputStream(file)));
        LOGGER.debug(s);
        return gson.fromJson(s, target);
    }

    protected void write(File destination, T entity) throws IOException {
        final String jsonString = gson.toJson(entity);
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(destination, false));
        out.write(jsonString);
        out.close();
    }
}
