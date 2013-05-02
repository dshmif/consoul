package com.abstractthis.consoul.commands;

import com.abstractthis.consoul.ConsoleOutPipe;
import com.abstractthis.consoul.commands.command.Command;
import com.abstractthis.consoul.commands.exception.CommandPerformException;

public class ClearCommand implements Command {
//	private static final String OS_NAME = System.getProperty("os.name");
	private static final String ESC = "\033[";

	public String getManual() {
		return "Clears the terminal.";
	}

	public String getUsage() {
		return "clear";
	}

	public void perform(ConsoleCommand command) throws CommandPerformException {
		// ANSI Escape Sequence support is required for this to work
		ConsoleOutPipe out = command.getCommandOutputPipe();
		out.useNewline(false);
		out.sendAndFlush(ESC + "2J");
		out.useNewline(true);
	}

	public boolean verifyArguments(String[] cmdArgs) {
		return cmdArgs.length == 0;
	}

}
