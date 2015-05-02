package org.foomla.api.converters;

import java.util.Map.Entry;
import java.util.Set;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.data.MediaType;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;

@SuppressWarnings("rawtypes")
public class CustomJacksonRepresentation<T> extends JacksonRepresentation {

    private final EntityRestMapping entityRestMapping;

    @SuppressWarnings("unchecked")
    public CustomJacksonRepresentation(MediaType mediaType, T object, EntityRestMapping entityRestMapping) {
        super(mediaType, object);
        this.entityRestMapping = entityRestMapping;
    }

    @SuppressWarnings("unchecked")
    public CustomJacksonRepresentation(Representation source, Class<T> objectClass, EntityRestMapping entityRestMapping) {
        super(source, objectClass);
        this.entityRestMapping = entityRestMapping;
    }

    @Override
    protected ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = super.createObjectMapper();
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Set<Entry<Class, Class>> entrySet = entityRestMapping.getMapping().entrySet();
        for (Entry<Class, Class> entry : entrySet) {
            objectMapper.getDeserializationConfig().addMixInAnnotations(entry.getKey(), entry.getValue());
        }
        return objectMapper;
    }
}
