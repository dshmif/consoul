package com.abstractthis.consoul.commands;

import com.abstractthis.consoul.ConsoleOutPipe;
import com.abstractthis.consoul.ConsoleProperties;
import com.abstractthis.consoul.commands.command.Command;
import com.abstractthis.consoul.commands.exception.CommandPerformException;

public class GetPropertyCommand implements Command {

	public String getManual() {
		StringBuilder manPage = new StringBuilder(128);
		manPage.append("Displays the property value for the property name specified.\n")
		       .append('\n')
		       .append("Parameters:\n")
		       .append("Provide the name of the property.");
		System.out.println(manPage.length());
		return manPage.toString();
	}

	public String getUsage() {
		return "command:getProp|command:getProperty [<property_name>]";
	}

	public void perform(ConsoleCommand command) throws CommandPerformException {
		ConsoleOutPipe outPipe = command.getCommandOutputPipe();
		String[] args = command.getCommandArguments();
		ConsoleProperties props = ConsoleProperties.getConsoleProperties();
		final String prop = props.getProperty(args[0]);
		if( prop == null ) {
			outPipe.sendAndFlush("'" + args[0] + "' property not found.");
		}
		else {
			String msg = String.format("Property value for '%s' is %s", args[0], prop);
			outPipe.sendAndFlush(msg);
		}
	}

	public boolean verifyArguments(String[] cmdArgs) {
		return cmdArgs.length == 1;
	}

}
