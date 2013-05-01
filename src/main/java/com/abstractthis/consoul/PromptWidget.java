package com.abstractthis.consoul;

import java.io.PrintStream;

import com.abstractthis.consoul.widgets.Widget;

final class PromptWidget implements Widget {
	private final ConsoleDisplay display;
	
	public PromptWidget(ConsoleDisplay display) {
		this.display = display;
	}

	public void render(PrintStream stream) {
		stream.print(display.getPromptText() + ">");
		display.attach();
	}

}
