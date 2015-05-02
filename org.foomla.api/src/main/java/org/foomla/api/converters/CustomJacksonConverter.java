package org.foomla.api.converters;

import org.apache.log4j.Logger;

import org.restlet.data.MediaType;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;

public class CustomJacksonConverter extends JacksonConverter {
    
    private static final Logger logger = Logger.getLogger(CustomJacksonConverter.class);

    private final EntityRestMapping entityRestMapping;
    
    public CustomJacksonConverter(EntityRestMapping entityRestMapping) {
        this.entityRestMapping = entityRestMapping;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> JacksonRepresentation<T> create(MediaType mediaType, T source) {
        logger.debug("Get mapping for '" + source + "'");
        return new CustomJacksonRepresentation<Object>(mediaType, source, entityRestMapping);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> JacksonRepresentation<T> create(Representation source, Class<T> objectClass) {
        return new CustomJacksonRepresentation<T>(source, objectClass, entityRestMapping);
    }
}
