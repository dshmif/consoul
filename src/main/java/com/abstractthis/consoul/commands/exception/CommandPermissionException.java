package com.abstractthis.consoul.commands.exception;

public class CommandPermissionException extends Exception {

	private static final long serialVersionUID = 654094380815376766L;

	/**
     * Creates a <code>CommandPermissionException</code> with no message.
     */
    public CommandPermissionException() {
        super();
    }

    /**
     * Creates a <code>CommandPermissionException</code> with the specified
     * message.
     * @param msg exception message.
     */
    public CommandPermissionException(String msg) {
        super(msg);
    }

    /**
     * Creates a <code>CommandPermissionException</code> with the specified
     * message and cause.
     * @param msg exception message.
     * @param cause the reason the exception occurred.
     */
    public CommandPermissionException(String msg, Throwable cause) {
        super(msg,cause);
    }

    /**
     * Creates a <code>CommandPermissionException</code> with the specified
     * cause.
     * @param cause the reason the exception occurred.
     */
    public CommandPermissionException(Throwable cause) {
        super(cause);
    }
    
}
