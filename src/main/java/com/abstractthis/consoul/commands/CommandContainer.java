package com.abstractthis.consoul.commands;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import com.abstractthis.consoul.ApplicationContext;
import com.abstractthis.consoul.commands.command.Command;
import com.abstractthis.consoul.commands.command.ContextAwareCommand;
import com.abstractthis.consoul.commands.exception.CommandInitException;
import com.abstractthis.consoul.commands.exception.CommandNotFoundException;

public final class CommandContainer {
	
	/* Maps the command name to its implementation class name. */
    private final Map<String, CommandDefinition> commands;
	
	/* Command creation static error messages */
    private static final String CMD_CLASS_NOT_FOUND =
            "Specified Command does not exist.";
    private static final String CMD_ACCESS_DENIED =
            "System isn't allowing access to the Command.";
    private static final String CMD_CREATION_FAILED =
            "System couldn't create the Command.";
    
    public CommandContainer(InputStream validationConfigFileStream,
    		InputStream parseConfigFileStream) {
        CommandsConfigParser parser =
        	new CommandsConfigParser(validationConfigFileStream, parseConfigFileStream);
        commands = parser.getCommandsMap();
    }

	public Command getCommand(ConsoleCommand consoleCmd)
	throws CommandInitException, CommandNotFoundException {
		return this.getCommand(consoleCmd.getCommandName(),
				               consoleCmd.getApplicationContext());
	}
	
	public Command getCommand(String cmdName, ApplicationContext context)
	throws CommandInitException, CommandNotFoundException {
		try {
			CommandDefinition cmdDef = commands.get(cmdName);
			String className = cmdDef.getImplClassName();
			if( className == null ) {
				throw new CommandNotFoundException(CMD_CLASS_NOT_FOUND);
			}
            Class<?> cmdClass = Class.forName(className);
            Command cmd = (Command)cmdClass.newInstance();
            if( cmd instanceof ContextAwareCommand ) {
            	((ContextAwareCommand) cmd).setContext(context);
            }
            return cmd;
        }
        catch(ClassNotFoundException cnfEx) {
            throw new CommandNotFoundException(CMD_CLASS_NOT_FOUND, cnfEx);
        }
        catch(IllegalAccessException iaEx) {
            throw new CommandInitException(CMD_ACCESS_DENIED, iaEx);
        }
        catch(InstantiationException iEx) {
            throw new CommandInitException(CMD_CREATION_FAILED, iEx);
        }
	}
	
	public String[] getSupportedCommandNames() {
		Set<String> cmdNames = commands.keySet();
		String[] names = cmdNames.toArray(new String[0]);
		Arrays.sort(names);
		return names;
	}
	
}
