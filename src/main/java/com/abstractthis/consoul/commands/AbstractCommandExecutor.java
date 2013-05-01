package com.abstractthis.consoul.commands;

import java.io.InputStream;

import com.abstractthis.consoul.ApplicationContext;
import com.abstractthis.consoul.ConsoleException;
import com.abstractthis.consoul.ConsoleOutPipe;
import com.abstractthis.consoul.commands.command.Command;
import com.abstractthis.consoul.commands.command.SecureCommand;
import com.abstractthis.consoul.commands.exception.CommandInitException;
import com.abstractthis.consoul.commands.exception.CommandNotFoundException;
import com.abstractthis.consoul.commands.exception.CommandPerformException;
import com.abstractthis.consoul.ini.ExecutorConfig;
import com.abstractthis.consoul.widgets.ManualWidget;


/**
 * Instances of this class are provided input from the console once
 * the user enters text and presses ENTER.
 * 
 * @author David Smith
 *
 */
public abstract class AbstractCommandExecutor {
	private static final String[] internalCmds = new String[]{"command:?",
		                                                      "command:usage",
		                                                      "command:list",
		                                                      "command:man",
		                                                      "command:listProps",
		                                                      "command:listProperties",
		                                                      "command:getProp",
		                                                      "command:getProperty",
		                                                      "command:sudoers"};
	private CommandContainer supportedCmds;
	
	public void destroy() { /* Nothing happens by default */ }
	
	public void init(ExecutorConfig config) {
		String cmdConfigPath = config.getCommandConfigPath();
		InputStream validateStream = ClassLoader.getSystemResourceAsStream(cmdConfigPath);
		InputStream parseStream = ClassLoader.getSystemResourceAsStream(cmdConfigPath);
		this.supportedCmds = new CommandContainer(validateStream, parseStream);
	}

	public void processCommand(ConsoleCommand command) {
		try {
			Command cmd = this.isInternalCommand(command) ?
					      this.getInternalCommand(command) :
					      this.getCommand(command);
			String[] cmdArgs = command.getCommandArguments();
			if( cmd.verifyArguments(cmdArgs) ) {
				// If the command is secure prompt for creds and verify them
				// before executing the command
				if( cmd instanceof SecureCommand ) {
					SecureCommand secCmd = (SecureCommand) cmd;
					CommandCreds creds = secCmd.promptForCredentials(command.getCommandOutputPipe());
					boolean isAllowed = secCmd.verifyCredentials(creds);
					if( !isAllowed ) {
						throw new CommandPerformException(
								"Not Authorized to execute command: " + command.getCommandName());
					}
					secCmd.perform(command);
				}
				// Standard command can just be executed
				else {
					cmd.perform(command);
				}
			}
			else {
				ConsoleOutPipe outPipe = command.getCommandOutputPipe();
				outPipe.sendAndFlush("Usage: " + cmd.getUsage());
				return;
			}
		}
		catch(CommandInitException initExcep) {
			command.getCommandOutputPipe().sendAndFlush(initExcep.getMessage());
			this.handleCommandInitException(command, initExcep);
		}
		catch(CommandNotFoundException notFndExcep) {
			command.getCommandOutputPipe().sendAndFlush(notFndExcep.getMessage());
			this.handleCommandNotFoundException(command, notFndExcep);
		}
		catch(CommandPerformException performExcep) {
			command.getCommandOutputPipe().sendAndFlush(performExcep.getMessage());
			this.handleCommandPerformException(command, performExcep);
		}
		catch(ConsoleException ce) {
			ConsoleOutPipe outPipe = command.getCommandOutputPipe();
			outPipe.sendAndFlush("Unexpected error trying to execute command " + command.getCommandName());
		}
	}
	
	private boolean isInternalCommand(ConsoleCommand command) {
		final String cmdName = command.getCommandName();
		for(String internalCmd : internalCmds) {
			if( cmdName.equals(internalCmd) ) {
				return true;
			}
		}
		return false;
	}
	
	private Command getInternalCommand(ConsoleCommand command) {
		String cmdName = command.getCommandName();
		Command cmd = null;
		if( cmdName.endsWith("?") || cmdName.endsWith("usage") ) {
			cmd = new UsageCommand();
		}
		else if( cmdName.endsWith("list") ) {
			cmd = new ListCommand();
		}
		else if( cmdName.endsWith("man") ) {
			cmd = new ManPageCommand();
		}
		else if( cmdName.endsWith("listProps") || cmdName.endsWith("listPropeties") ) {
			cmd = new ListPropertiesCommand();
		}
		else if( cmdName.endsWith("getProp") || cmdName.endsWith("getProperty") ) {
			cmd = new GetPropertyCommand();
		}
		else if( cmdName.endsWith("sudoers") ) {
			cmd = new SudoersCommand();
		}
		return cmd;
	}
	
	private Command getCommand(ConsoleCommand consoleCmd)
	throws CommandInitException, CommandNotFoundException {
		Command cmd = supportedCmds.getCommand(consoleCmd);
		return cmd;
	}
	
	private Command getCommand(String cmdName, ApplicationContext context)
	throws CommandInitException, CommandNotFoundException {
		Command cmd = supportedCmds.getCommand(cmdName, context);
		return cmd;
	}
	
	protected abstract void handleCommandPerformException(ConsoleCommand command, Throwable t);
    protected abstract void handleCommandPermissionException(ConsoleCommand command, Throwable t);
    protected abstract void handleCommandNotFoundException(ConsoleCommand command, Throwable t);
    protected abstract void handleCommandInitException(ConsoleCommand command, Throwable t);
    
    private class ListCommand implements Command {
    	
    	public String getManual() {
    		return null;
    	}
    	
    	public String getUsage() {
    		return "command:list";
    	}
    	
    	public void perform(ConsoleCommand command) throws CommandPerformException {
    		ConsoleOutPipe outPipe = command.getCommandOutputPipe();
    		String[] supportedCmdNames = AbstractCommandExecutor.this.supportedCmds.getSupportedCommandNames();
    		for(String cmdName : supportedCmdNames) {
    			outPipe.send(cmdName);
    		}
    		outPipe.flush();
    	}
    	
    	public boolean verifyArguments(String[] cmdArgs) {
    		return cmdArgs.length == 0;
    	}
    	
    }
    
    private class UsageCommand implements Command {
    	
    	public String getManual() {
    		return null;
    	}
    	
    	public String getUsage() {
    		return "command:?|command:usage [<command_name>]";
    	}
    	
    	public void perform(ConsoleCommand command) throws CommandPerformException {
    		String[] args = command.getCommandArguments();
    		ConsoleOutPipe outPipe = command.getCommandOutputPipe();
    		try {
    			ApplicationContext ctxt = command.getApplicationContext();
    			Command usageNeeded = AbstractCommandExecutor.this.getCommand(args[0], ctxt);
    			outPipe.sendAndFlush("Usage: " + usageNeeded.getUsage());
    		}
    		catch(CommandInitException cie) {
    			outPipe.sendAndFlush("Usage information couldn't be provided");
    		}
    		catch(CommandNotFoundException cnfe) {
    			outPipe.sendAndFlush("Usage not found for command " + args[0]);
    		}
    	}
    	
    	public boolean verifyArguments(String[] cmdArgs) {
    		return cmdArgs.length == 1;
    	}
    	
    }
    
    private class ManPageCommand implements Command {
    	
    	public String getManual() {
    		return null;
    	}
    	
    	public String getUsage() {
    		return "command:man [<command_name>]";
    	}
    	
    	public void perform(ConsoleCommand command) throws CommandPerformException {
    		String[] args = command.getCommandArguments();
    		ConsoleOutPipe outPipe = command.getCommandOutputPipe();
    		try {
    			ApplicationContext ctxt = command.getApplicationContext();
    			Command manPageNeeded = AbstractCommandExecutor.this.getCommand(args[0], ctxt);
    			String manPage = manPageNeeded.getManual();
    			if( manPage == null || manPage.length() == 0 ) {
    				outPipe.sendAndFlush("No manual for " + args[0]);
    			}
    			else {
    				outPipe.displayWidget(new ManualWidget(args[0], manPage));
    			}
    		}
    		catch(CommandInitException cie) {
    			outPipe.sendAndFlush("Manual information couldn't be provided");
    		}
    		catch(CommandNotFoundException cnfe) {
    			outPipe.sendAndFlush("Manual not found for command " + args[0]);
    		}
    	}
    	
    	public boolean verifyArguments(String[] cmdArgs) {
    		return cmdArgs.length == 1;
    	}
    	
    }
    
}