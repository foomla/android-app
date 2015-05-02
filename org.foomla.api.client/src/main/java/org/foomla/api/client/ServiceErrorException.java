package org.foomla.api.client;

import org.foomla.api.services.ServiceError;
import org.foomla.api.services.ServiceErrorType;
import org.restlet.resource.ResourceException;

public class ServiceErrorException extends RemoteInvocationException {

    private final ServiceError serviceError;

    public ServiceErrorException(ServiceError serviceError, ResourceException ex) {
        super(serviceError.getType().name() + " - " + serviceError.getDescription(), ex);
        this.serviceError = serviceError;
    }

    public String getDescription() {
        return serviceError.getDescription();
    }

    public ServiceError getServiceError() {
        return serviceError;
    }

    public ServiceErrorType getType() {
        return serviceError.getType();
    }

}
