package org.foomla.api.converters;

import java.util.HashMap;
import java.util.Map;

public abstract class EntityRestMapping {

    @SuppressWarnings("rawtypes")
    private static Map<Class, Class> mapping = new HashMap<Class, Class>();

    @SuppressWarnings("rawtypes")
    public static void map(Class class1, Class class2) {
        mapping.put(class1, class2);
    }

    @SuppressWarnings("rawtypes")
    public Map<Class, Class> getMapping() {
        return mapping;
    }
}
