package com.abstractthis.consoul;

public class ConsoleException extends RuntimeException {
	private static final long serialVersionUID = -1923695939351951533L;

	/**
     * Creates a <code>ConsoleException</code> with no message.
     */
    public ConsoleException() {
        super();
    }

    /**
     * Creates a <code>ConsoleException</code> with the specified
     * message.
     * @param msg exception message.
     */
    public ConsoleException(String msg) {
        super(msg);
    }

    /**
     * Creates a <code>ConsoleException</code> with the specified
     * message and cause.
     * @param msg exception message.
     * @param cause the reason the exception occurred.
     */
    public ConsoleException(String msg, Throwable cause) {
        super(msg,cause);
    }

    /**
     * Creates a <code>ConsoleException</code> with the specified
     * cause.
     * @param cause the reason the exception occurred.
     */
    public ConsoleException(Throwable cause) {
        super(cause);
    }
    
}
