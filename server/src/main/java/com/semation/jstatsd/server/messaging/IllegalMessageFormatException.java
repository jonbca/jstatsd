package com.semation.jstatsd.server.messaging;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 20/03/2012
 * Time: 18:13
 */
public class IllegalMessageFormatException extends Exception {
    private static final long serialVersionUID = -3784963944097567660L;

    public IllegalMessageFormatException(String message) {
        super(message);
    }
}
