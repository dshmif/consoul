package com.abstractthis.consoul.commands.command;

import com.abstractthis.consoul.commands.ConsoleCommand;
import com.abstractthis.consoul.commands.exception.CommandPerformException;

public interface Command {
	
	public String getManual();
	public String getUsage();
	public void perform(ConsoleCommand command) throws CommandPerformException;
	public boolean verifyArguments(String[] cmdArgs);

}
