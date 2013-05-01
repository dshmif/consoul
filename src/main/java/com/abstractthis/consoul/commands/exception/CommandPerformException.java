package com.abstractthis.consoul.commands.exception;

public class CommandPerformException extends Exception {

	private static final long serialVersionUID = -2709757124473004231L;

	/**
     * Creates a <code>CommandPerformException</code> with no message.
     */
    public CommandPerformException() {
        super();
    }

    /**
     * Creates a <code>CommandPerformException</code> with the specified
     * message.
     * @param msg exception message.
     */
    public CommandPerformException(String msg) {
        super(msg);
    }

    /**
     * Creates a <code>CommandPerformException</code> with the specified
     * message and cause.
     * @param msg exception message.
     * @param cause the reason the exception occurred.
     */
    public CommandPerformException(String msg, Throwable cause) {
        super(msg,cause);
    }

    /**
     * Creates a <code>CommandPerformException</code> with the specified
     * cause.
     * @param cause the reason the exception occurred.
     */
    public CommandPerformException(Throwable cause) {
        super(cause);
    }
    
}
