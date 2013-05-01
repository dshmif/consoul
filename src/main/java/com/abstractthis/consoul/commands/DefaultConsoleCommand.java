package com.abstractthis.consoul.commands;

import com.abstractthis.consoul.ApplicationContext;
import com.abstractthis.consoul.ConsoleOutPipe;

public final class DefaultConsoleCommand implements ConsoleCommand {
	private final String commandName;
	private final String[] commandArgs;
	private ApplicationContext context;
	private ConsoleOutPipe outputPipe;

	public DefaultConsoleCommand(String cmdName, String[] cmdParams) {
		this.commandName = cmdName;
		this.commandArgs = cmdParams;
	}
	
	public ApplicationContext getApplicationContext() {
		return this.context;
	}
	
	public String getCommandName() {
		return this.commandName;
	}
	
	public ConsoleOutPipe getCommandOutputPipe() {
		return this.outputPipe;
	}

	public String[] getCommandArguments() {
		return this.commandArgs;
	}
	
	public void setApplicationContext(ApplicationContext cxt) {
		this.context = cxt;
	}
	
	public void setCommandOutputPipe(ConsoleOutPipe pipe) {
		this.outputPipe = pipe;
	}

}
