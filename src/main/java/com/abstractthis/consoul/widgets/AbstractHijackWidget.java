package com.abstractthis.consoul.widgets;

import java.io.PrintStream;

public abstract class AbstractHijackWidget implements DisplayHijackWidget {
	private PrintStream renderStream;
	private Thread renderThread;
	
	public AbstractHijackWidget() {
		renderThread = new Thread(new Runnable() {
			public void run() {
				try {
					while( !Thread.currentThread().isInterrupted() ) {
						AbstractHijackWidget.this.hijackRender(renderStream);
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
