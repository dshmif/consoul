package com.abstractthis.consoul.ini;

public class InitializerException extends RuntimeException {

	private static final long serialVersionUID = 1496068536725932072L;
	
	public InitializerException() {
        super();
    }

    public InitializerException(String msg) {
        super(msg);
    }

    public InitializerException(Throwable cause) {
        super(cause);
    }

    public InitializerException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
