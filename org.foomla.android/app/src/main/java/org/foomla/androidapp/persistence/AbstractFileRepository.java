package org.foomla.androidapp.persistence;

import org.foomla.api.client.ClientEntityRestMapping;
import org.foomla.api.converters.CustomJacksonConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

public abstract class AbstractFileRepository<T extends Serializable> implements Repository<T> {

    protected Object convert(File file, Class target) throws IOException {
        CustomJacksonConverter converter = new CustomJacksonConverter(new ClientEntityRestMapping());
        return converter.getObjectMapper().readValue(new FileInputStream(file), target);
    }

    protected void write(File destination, T entity) throws IOException {
        CustomJacksonConverter converter = new CustomJacksonConverter(new ClientEntityRestMapping());
        converter.getObjectMapper().writeValue(destination, entity);
    }
}
