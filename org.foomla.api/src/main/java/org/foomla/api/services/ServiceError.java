package org.foomla.api.services;

import org.codehaus.jackson.map.ObjectMapper;

public class ServiceError {

    public static final int FOOMLA_SERVICE_ERROR_CODE = 450;

    public static ServiceError fromJson(String jsonText) {
        try {
            return new ObjectMapper().readValue(jsonText, ServiceError.class);
        }
        catch (Exception ex) {
            return new ServiceError(ServiceErrorType.UNKOWN, "Client error.");
        }
    }
    private final String description;

    private final ServiceErrorType type;

    public ServiceError(ServiceErrorType type, String description) {
        if (type == null) {
            throw new IllegalArgumentException("Service error type must not be null");
        }
        this.type = type;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public ServiceErrorType getType() {
        return type;
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        }
        catch (Exception ex) {
            return "";
        }
    }
}
