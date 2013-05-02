package com.abstractthis.consoul;

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
	 * The specified single character will be sent to the display
	 * for rendering.
	 * @param c
	 * @return this <code>ConsoleOutPipe</code> for chaining
	 */
	public ConsoleOutPipe sendAndFlushChar(char c) {
		display.render(c);
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

