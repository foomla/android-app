package org.foomla.api.client;

public class RemoteInvocationException extends ClientConnectionException {

    public RemoteInvocationException(String message) {
        super(message);
    }

    public RemoteInvocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteInvocationException(Throwable cause) {
        super(cause);
    }

}
