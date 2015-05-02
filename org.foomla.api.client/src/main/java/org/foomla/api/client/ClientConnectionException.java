package org.foomla.api.client;

public class ClientConnectionException extends RuntimeException {

    public ClientConnectionException(String message) {
        super(message);
    }

    public ClientConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientConnectionException(Throwable cause) {
        super(cause);
    }

}
