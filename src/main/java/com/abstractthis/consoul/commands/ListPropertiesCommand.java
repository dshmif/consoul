package com.abstractthis.consoul.commands;

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

import com.abstractthis.consoul.ConsoleOutPipe;
import com.abstractthis.consoul.ConsoleProperties;
import com.abstractthis.consoul.commands.command.Command;
import com.abstractthis.consoul.commands.exception.CommandPerformException;

public class ListPropertiesCommand implements Command {

	public String getManual() {
		StringBuilder manPage = new StringBuilder(128);
		manPage.append("Displays all of the application property names.\n")
		       .append('\n')
		       .append("Parameters:\n")
		       .append("No parameters are accepted for this command.");
		return manPage.toString();
	}

	public String getUsage() {
		return "command:listProps|command:listProperties";
	}

	public void perform(ConsoleCommand command) throws CommandPerformException {
		ConsoleProperties props = ConsoleProperties.getConsoleProperties();
		String[] propNames = props.getPropertyNames();
		ConsoleOutPipe outPipe = command.getCommandOutputPipe();
		if( propNames.length == 0 ) {
			outPipe.sendAndFlush("There are no application properties.");
		}
		else {
			for(String name : propNames) {
				outPipe.sendAndFlush(name);
			}
		}
	}

	public boolean verifyArguments(String[] cmdArgs) {
		return cmdArgs.length == 0;
	}

}
