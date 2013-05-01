package com.abstractthis.consoul.ini;

public class CommandLineArgException extends RuntimeException {

	private static final long serialVersionUID = -3230166735851614368L;

	public CommandLineArgException() {
		super();
	}

	public CommandLineArgException(String message) {
		super(message);
	}

	public CommandLineArgException(Throwable cause) {
		super(cause);
	}

	public CommandLineArgException(String message, Throwable cause) {
		super(message, cause);
	}

}
