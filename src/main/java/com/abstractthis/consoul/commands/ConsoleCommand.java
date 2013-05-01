package com.abstractthis.consoul.commands;

import com.abstractthis.consoul.ApplicationContext;
import com.abstractthis.consoul.ConsoleOutPipe;

public interface ConsoleCommand {

	public ApplicationContext getApplicationContext();
	public String getCommandName();
	public String[] getCommandArguments();
	public ConsoleOutPipe getCommandOutputPipe();
	public void setApplicationContext(ApplicationContext context);
	public void setCommandOutputPipe(ConsoleOutPipe pipe);
	
}
