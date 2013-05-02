package com.abstractthis.consoul.widgets;

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

import java.io.PrintStream;

public abstract class AbstractHijackWidget implements DisplayHijackWidget {
	private PrintStream renderStream;
	private Thread renderThread;
	
	public AbstractHijackWidget(final boolean isSinglePass) {
		renderThread = new Thread(new Runnable() {
			public void run() {
				try {
					while( !Thread.currentThread().isInterrupted() ) {
						AbstractHijackWidget.this.hijackRender(renderStream);
						if( isSinglePass ) break;
					}
				}
				catch(InterruptedException ignored) {
					// This code defines the interrupt policy
					// for this thread so we can safely ignore
					// and allow the thread to exit on interrupt.
				}
			}
		});
	}

	public void stop() {
		renderThread.interrupt();
	}

	public void render(PrintStream stream) {
		this.renderStream = stream;
		renderThread.start();
		try {
			renderThread.join();
		}
		catch(InterruptedException ie) {
			// The ConsoleDisplay ExecutorService defines the
			// interrupt policy for the thread that called the
			// render method, so we need to set the interrupted
			// flag so that code higher up the call stack can
			// handle it.
			Thread.currentThread().interrupt();
		}
	}
	
	public abstract void hijackRender(PrintStream stream)
		throws InterruptedException;

}
