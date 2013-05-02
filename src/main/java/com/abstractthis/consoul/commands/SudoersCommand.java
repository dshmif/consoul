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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

import com.abstractthis.consoul.ApplicationContext;
import com.abstractthis.consoul.ConsoleOutPipe;
import com.abstractthis.consoul.ContextVariable;
import com.abstractthis.consoul.commands.command.ContextAwareCommand;
import com.abstractthis.consoul.commands.command.SecureCommand;
import com.abstractthis.consoul.commands.exception.CommandPerformException;
import com.abstractthis.consoul.ini.ConsoleInitializer;
import com.abstractthis.consoul.util.BCrypt;

public class SudoersCommand implements SecureCommand, ContextAwareCommand {
	private static final String PROMPT_FOR_USERNAME = "User:";
	private static final String PROMPT_FOR_PASSWORD = "Password:";
	
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
			String[] cmdArgs = command.getCommandArguments();
			String newSudoerHash = BCrypt.hashpw(cmdArgs[0] + cmdArgs[1], BCrypt.gensalt(12));
			bw.write(newSudoerHash);
			bw.write(',');
			bw.flush();
			// Add the new sudoer to the context stored sudoer list
			@SuppressWarnings("unchecked")
			ContextVariable<Set<String>> ctxtSudoersSet =
					(ContextVariable<Set<String>>) context.get(
							ConsoleInitializer.getInitializer().getSudoersPropKey());
			Set<String> ctxtSudoers = ctxtSudoersSet.getContent();
			ctxtSudoers.add(newSudoerHash);
			out.sendAndFlush("user added to sudoers list.");
		}
		catch(IOException ioe) {
			throw new CommandPerformException("Failed to add user to sudoers list!", ioe);
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
		out.useNewline(false);
		out.sendAndFlush(PROMPT_FOR_USERNAME);
		Scanner inputScan = new Scanner(System.in);
		String username = inputScan.nextLine();
		out.sendAndFlush(PROMPT_FOR_PASSWORD);
		String password = inputScan.nextLine();
		out.useNewline(true);
		return new CommandCreds(username, password);
	}

	public boolean verifyCredentials(CommandCreds creds) {
		return this.context.isSudoer(creds);
	}

}
