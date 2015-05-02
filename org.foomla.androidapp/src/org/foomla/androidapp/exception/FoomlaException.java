package org.foomla.androidapp.exception;


public class FoomlaException extends RuntimeException {

    public FoomlaException(String msg) {
        super(msg);
    }

    public FoomlaException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
