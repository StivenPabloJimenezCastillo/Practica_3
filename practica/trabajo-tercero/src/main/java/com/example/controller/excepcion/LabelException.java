package com.example.controller.excepcion;

public class LabelException extends Exception {
    /**
     * Creates a new instance of <code>EmptyException</code> without detail
     * message.
     */
    public LabelException() {
    }

    /**
     * Constructs an instance of <code>EmptyException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public LabelException(String msg) {
        super(msg);
    }
}
