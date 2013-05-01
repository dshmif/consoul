package com.abstractthis.consoul.widgets;

public interface DisplayHijackWidget extends Widget {

	/**
	 * Provides a way to make the widget release its hold
	 * (hijack) on the display.
	 */
	public void stop();
	
}
