package com.semation.jstatsd.server.messaging;

public class IllegalMessageFormatException extends Exception {
    private static final long serialVersionUID = -3784963944097567660L;

    public IllegalMessageFormatException(String message) {
        super(message);
    }
}
