package com.abstractthis.consoul.commands.command;

import com.abstractthis.consoul.ConsoleOutPipe;
import com.abstractthis.consoul.commands.CommandCreds;

public interface SecureCommand extends Command {

	public CommandCreds promptForCredentials(ConsoleOutPipe out);
	public boolean verifyCredentials(CommandCreds creds);
	
}
