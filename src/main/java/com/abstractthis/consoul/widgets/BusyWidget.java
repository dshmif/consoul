package com.abstractthis.consoul.widgets;

import java.io.PrintStream;

public class BusyWidget extends AbstractHijackWidget {
	private final long DELAY_MILLISECONDS;
	
	public BusyWidget() {
		this(250);
	}
	
	public BusyWidget(long delayMillis) {
		super();
		DELAY_MILLISECONDS = delayMillis;
	}

	@Override
	public void hijackRender(PrintStream stream) throws InterruptedException {
		try {
			stream.print('-');
			Thread.sleep(DELAY_MILLISECONDS);
			stream.print('\b');
			stream.print('\\');
			Thread.sleep(DELAY_MILLISECONDS);
			stream.print('\b');
			stream.print('/');
			Thread.sleep(DELAY_MILLISECONDS);
			stream.print('\b');
		}
		catch(InterruptedException ie) {
			stream.print('\b');
			throw ie;
		}
	}

}
