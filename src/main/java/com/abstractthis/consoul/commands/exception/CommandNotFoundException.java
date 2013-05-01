package com.abstractthis.consoul.commands.exception;

public class CommandNotFoundException extends Exception {

	private static final long serialVersionUID = -2801729939853021215L;

	/**
     * Creates a <code>CommandNotFoundException</code> with no message.
     */
    public CommandNotFoundException() {
        super();
    }

    /**
     * Creates a <code>CommandNotFoundException</code> with the specified
     * message.
     * @param msg exception message.
     */
    public CommandNotFoundException(String msg) {
        super(msg);
    }

    /**
     * Creates a <code>CommandNotFoundException</code> with the specified
     * message and cause.
     * @param msg exception message.
     * @param cause the reason the exception occurred.
     */
    public CommandNotFoundException(String msg, Throwable cause) {
        super(msg,cause);
    }

    /**
     * Creates a <code>CommandNotFoundException</code> with the specified
     * cause.
     * @param cause the reason the exception occurred.
     */
    public CommandNotFoundException(Throwable cause) {
        super(cause);
    }
    
}
