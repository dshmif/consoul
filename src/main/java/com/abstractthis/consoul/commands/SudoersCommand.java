package com.abstractthis.consoul.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import com.abstractthis.consoul.ApplicationContext;
import com.abstractthis.consoul.ConsoleOutPipe;
import com.abstractthis.consoul.ContextVariable;
import com.abstractthis.consoul.commands.command.ContextAwareCommand;
import com.abstractthis.consoul.commands.command.SecureCommand;
import com.abstractthis.consoul.commands.exception.CommandPerformException;
import com.abstractthis.consoul.ini.ConsoleInitializer;
import com.abstractthis.consoul.widgets.SudoWidget;

public class SudoersCommand implements SecureCommand, ContextAwareCommand {
	
	private ApplicationContext context;

	public String getManual() {
		StringBuilder manPage = new StringBuilder(128);
		manPage.append("Adds the specified user with the specified credentials to the sudoers list.\n")
		       .append('\n')
		       .append("Parameters:\n")
		       .append("username - The new sudo enabled users username.\n")
		       .append("password - The new sudo enabled users password.");
		return manPage.toString();
	}

	public String getUsage() {
		return "command:sudoers [username] [password]";
	}

	public void perform(ConsoleCommand command) throws CommandPerformException {
		final String SUDOER_FILE_PATH = ConsoleInitializer.getInitializer().getSudoersPath();
		ConsoleOutPipe out = command.getCommandOutputPipe();
		out.sendAndFlush("Adding user to the sudoers list...");
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(SUDOER_FILE_PATH), true));
			String newSudoer = context.removeCurrentSudoer();
			bw.write(newSudoer);
			bw.write(',');
			bw.flush();
			// Add the new sudoer to the context stored sudoer list
			@SuppressWarnings("unchecked")
			ContextVariable<Set<String>> ctxtSudoersSet =
					(ContextVariable<Set<String>>) context.get(
							ConsoleInitializer.getInitializer().getSudoersPropKey());
			Set<String> ctxtSudoers = ctxtSudoersSet.getContent();
			ctxtSudoers.add(newSudoer);
			out.sendAndFlush("user added to sudoers list.");
		}
		catch(IOException ioe) {
			throw new CommandPerformException("Failed to add user to sudoers list!");
		}
		finally {
			try { if( bw != null ) { bw.close(); } }
			catch(IOException ioe2) { /* NOP */ }
		}
	}

	public boolean verifyArguments(String[] cmdArgs) {
		return cmdArgs.length == 2;
	}

	public ApplicationContext getContext() {
		return this.context;
	}

	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	public CommandCreds promptForCredentials(ConsoleOutPipe out) {
		out.displayWidget(new SudoWidget(this.context));
		return null;
	}

	public boolean verifyCredentials(CommandCreds creds) {
		return context.isSudoer();
	}

}
