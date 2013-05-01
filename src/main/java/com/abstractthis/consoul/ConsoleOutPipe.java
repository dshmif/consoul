package com.abstractthis.consoul;

import java.util.ArrayList;
import java.util.List;

import com.abstractthis.consoul.widgets.Widget;


public final class ConsoleOutPipe {

	private final ConsoleDisplay display;
	private final List<String> fullOutput = new ArrayList<String>();
	private boolean newLine = true;
	
	public ConsoleOutPipe(ConsoleDisplay display) {
		this.display = display;
	}
	
	/**
	 * Pushes out each of the queued output to the display
	 * for rendering. The push occurs a single output <code>String</code>
	 * at a time.
	 */
	public ConsoleOutPipe flush() {
		display.render(fullOutput.toArray(new String[0]), newLine);
		fullOutput.clear();
		return this;
	}
	
	/**
	 * The specified widget is placed in the rendering queue
	 * to be rendered to the console display.
	 * @param widget
	 */
	public ConsoleOutPipe displayWidget(Widget widget) {
		display.render(widget);
		return this;
	}
	
	/**
	 * The provided output is placed in a queue which will
	 * be sent to the display for rendering once <code>clean</code>
	 * is called on this pipe.
	 * @param output
	 */
	public ConsoleOutPipe send(String output) {
		fullOutput.add(output);
		return this;
	}
	
	/**
	 * The provided output is immediately sent to the display
	 * for rendering.
	 * @param output
	 */
	public ConsoleOutPipe sendAndFlush(String output) {
		display.render(output, newLine);
		return this;
	}
	
	/**
	 * Tells the output to append a newline to the output or not.
	 * @param yesNo true if the output appends a newline
	 */
	public void useNewline(boolean yesNo) {
		this.newLine = yesNo;
	}
	
}
