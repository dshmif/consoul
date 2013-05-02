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
import java.util.Scanner;

import com.abstractthis.consoul.ApplicationContext;
import com.abstractthis.consoul.commands.CommandCreds;

public class SudoWidget extends AbstractHijackWidget {
	private static final String PROMPT_FOR_USERNAME = "User:";
	private static final String PROMPT_FOR_PASSWORD = "Password:";
	
	private ApplicationContext context;
	
	public SudoWidget(ApplicationContext context) {
		super(true);
		this.context = context;
	}

	// Because of the fact that widgets are rendered to the display
	// in a separate thread a blocking widget implementation needs
	// to be implemented for this to be useful. For now implement
	// directly in SecureCommand.promptForCredentials. Look at
	// SudoersCommand for an example.
	public void hijackRender(PrintStream stream) {
		stream.print(PROMPT_FOR_USERNAME);
		Scanner inputScan = new Scanner(System.in);
		String username = inputScan.nextLine();
		stream.print(PROMPT_FOR_PASSWORD);
		String password = inputScan.nextLine();
		String msg = String.format("Widget User: %s Widget Password: %s", username, password);
		System.err.println(msg);
		context.setCurrentSudoer(new CommandCreds(username, password));
	}

}
