package com.abstractthis.consoul.commands.exception;

public class CommandInitException extends Exception {

	private static final long serialVersionUID = -2230008797837542794L;

	/**
     * Creates a <code>CommandInitException</code> with no message.
     */
    public CommandInitException() {
        super();
    }

    /**
     * Creates a <code>CommandInitException</code> with the specified
     * message.
     * @param msg exception message.
     */
    public CommandInitException(String msg) {
        super(msg);
    }

    /**
     * Creates a <code>CommandInitException</code> with the specified
     * message and cause.
     * @param msg exception message.
     * @param cause the reason the exception occurred.
     */
    public CommandInitException(String msg, Throwable cause) {
        super(msg,cause);
    }

    /**
     * Creates a <code>CommandInitException</code> with the specified
     * cause.
     * @param cause the reason the exception occurred.
     */
    public CommandInitException(Throwable cause) {
        super(cause);
    }
}
