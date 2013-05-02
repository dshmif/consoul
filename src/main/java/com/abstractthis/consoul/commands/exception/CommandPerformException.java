package com.abstractthis.consoul.commands.exception;

//The MIT License (MIT)
//
//Copyright (c) 2013 David Smith <www.abstractthis.com>
//
//Permission is hereby granted, free of charge, to any person obtaining a copy
//of this software and associated documentation files (the "Software"), to deal
//in the Software without restriction, including without limitation the rights
//to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the Software is
//furnished to do so, subject to the following conditions:
//
//The above copyright notice and this permission notice shall be included in
//all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
//OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
//THE SOFTWARE.

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
