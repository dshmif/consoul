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

import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import com.abstractthis.consoul.ApplicationContext;
import com.abstractthis.consoul.commands.command.Command;
import com.abstractthis.consoul.commands.command.ContextAwareCommand;
import com.abstractthis.consoul.commands.command.SecureCommand;
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
			if( cmdDef == null ) {
				throw new CommandNotFoundException(CMD_CLASS_NOT_FOUND);
			}
			String className = cmdDef.getImplClassName();
            Class<?> cmdClass = Class.forName(className);
            Command cmd = (Command)cmdClass.newInstance();
            this.verifyProperCommandType(cmdDef, cmd);
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
	
	private void verifyProperCommandType(CommandDefinition cmdDef, Command cmd)
			throws CommandInitException {
		if( cmdDef.isSecure() && !(cmd instanceof SecureCommand) ) {
			String errMsg = String.format("%s [%s] defined as secure but not implemented as such.",
					cmdDef.getName(), cmdDef.getImplClassName());
			throw new CommandInitException(errMsg);
		}
		else if( cmdDef.isContextAware() && !(cmd instanceof ContextAwareCommand)) {
			String errMsg = String.format("%s [%s] defined as contextAware but not implemented as such.",
					cmdDef.getName(), cmdDef.getImplClassName());
			throw new CommandInitException(errMsg);
		}
	}
	
	public String[] getSupportedCommandNames() {
		Set<String> cmdNames = commands.keySet();
		String[] names = cmdNames.toArray(new String[0]);
		Arrays.sort(names);
		return names;
	}
	
}

