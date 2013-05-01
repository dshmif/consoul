package com.abstractthis.consoul.commands;

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
