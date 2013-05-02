package com.abstractthis.consoul.ini;

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
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.abstractthis.consoul.ConsoleInputParser;
import com.abstractthis.consoul.ConsoleOutPipe;
import com.abstractthis.consoul.commands.AbstractCommandExecutor;
import com.abstractthis.consoul.util.BCrypt;

public final class ConsoleInitializer {
	private static final ConsoleInitializer INITIALIZER = new ConsoleInitializer();
	private static final List<InitializerListener> listeners = new ArrayList<InitializerListener>();
	private static final List<CommandLineArgDef> cmdLineArgDefs = new ArrayList<CommandLineArgDef>();
	private static final List<String[]> SUDOERS = new ArrayList<String[]>();
	private static final String INI_FILE_PATH = "CONSOLE-INI/console.xml";
	private static final String INI_PROP_FILE_PATH = "CONSOLE-INI/console.properties";
	private static final String SUDOERS_FILE_PATH = System.getProperty("user.dir") + "/sudoers";
	private static final String SUDOERS_PROP_KEY = "com.abstractthis.consoul.sudoers";
	private static final Properties INI_PROPS = new Properties();
	private static String applicationTitle;
	private static String promptText;
	private static ConsoleInputParser inputParser;
	private static ExecutorConfig exeConfig;
	private static AbstractCommandExecutor commandExecutor;
	private static CommandLineArgs cmdLineArgs;
	private static boolean hasBeenInitialized;
	
	// Private constructor enforces singleton
	private ConsoleInitializer() {}
	
	/**
	 * Returns the only <code>ConsoleInitializer</code>.
	 * @return the sole <code>ConsoleInitializer</code>
	 */
	public static ConsoleInitializer getInitializer() {
		return INITIALIZER;
	}
	
	public String getApplicationTitle() {
		return applicationTitle;
	}
	
	public String getPromptText() {
		return promptText;
	}
	
	public AbstractCommandExecutor getCommandExecutor() {
		return commandExecutor;
	}
	
	public ConsoleInputParser getInputParser() {
		return inputParser;
	}
	
	public CommandLineArgs getCommandLineArgs() {
		if( !hasBeenInitialized ) {
			String msg = "The application must be initialized before getting command line arguments.";
			throw new IllegalStateException(msg);
		}
		return cmdLineArgs;
	}
	
	/**
	 * Provides access to any of the application configuration properties.
	 * @param propName name of the property requested
	 * @return the requested property or <code>null</code> if the requested
	 * property doesn't exist
	 */
	public String getInitProperty(String propName) {
		return INI_PROPS.getProperty(propName);
	}
	
	public Enumeration<?> getInitPropertyNames() {
		return INI_PROPS.propertyNames();
	}
	
	public String getSudoersPath() {
		return SUDOERS_FILE_PATH;
	}
	
	public String getSudoersPropKey() {
		return SUDOERS_PROP_KEY;
	}
	
	/**
	 * Notifies all listeners to perform cleanup activities.
	 * @param outPipe allows the listeners to provide output
	 * to the console
	 */
	public void deinitialize(ConsoleOutPipe outPipe) {
		for(InitializerListener listener : listeners) {
			listener.deinitialize(outPipe);
		}
	}
	
	/**
	 * Handles all the initialization chores based on the
	 * command line arguments provided to the application.
	 * @param args
	 */
	public void initialize(String[] args) {
		if( hasBeenInitialized ) {
			String msg = "The application has already been initialized.";
			throw new IllegalStateException(msg);
		}
		try {
			this.loadGlobalProperties();
			this.loadDeploymentDescriptor();
			this.initSudoerFile();
		}
		catch(IOException ioe) {
			throw new InitializerException(ioe);
		}
		commandExecutor = this.createExecutor(exeConfig);
		commandExecutor.init(exeConfig);
		hasBeenInitialized = true;
	}
	
	/**
	 * Called to have all the initialize listeners notified
	 * that the application is initialized.
	 * @param outPipe allows the listeners to provide output
	 * to the console
	 */
	public void runInitializers(ConsoleOutPipe outPipe) {
		for(InitializerListener listener : listeners) {
			listener.initialize(outPipe);
		}
	}
	
	/**
	 * Determines whether the specified command line argument is valid
	 * for this console application.
	 * @param arg the argument to check validity of
	 * @return true if the arg is valid
	 */
	public boolean isValidCommandLineArg(CommandLineArg arg) {
		boolean isValid = false;
		for(CommandLineArgDef argDef : cmdLineArgDefs) {
			isValid = argDef.valid(arg);
			if( isValid ) break;
		}
		return isValid;
	}
	
	private void initSudoerFile() {
		boolean sudoersFileExists = new File(SUDOERS_FILE_PATH).exists();
		if( !sudoersFileExists ) {
			boolean sudoerFileCreated = this.createSudoerFile();
			if ( !sudoerFileCreated ) {
				throw new InitializerException("Failed to create sudoer file!");
			}
		}
	}
	
	private boolean createSudoerFile() {
		boolean createSuccess = true;
		BufferedWriter bw = null;
		try {
			File sudoersFile = new File(SUDOERS_FILE_PATH);
			bw = new BufferedWriter(new FileWriter(sudoersFile, true));
			bw.write(SUDOERS_PROP_KEY);
			bw.write("=");
			for(String[] cred : SUDOERS) {
				String flattenCred = cred[0] + cred[1];
				String hashedCred = BCrypt.hashpw(flattenCred, BCrypt.gensalt(12));
				bw.write(hashedCred);
				bw.write(',');
			}
			bw.flush();
			return createSuccess;
			
		}
		catch(IOException ioe) {
			createSuccess = false;
		}
		finally {
			try { if( bw != null ) bw.close(); }
			catch(IOException ioe2) { /* NOP */ }
		}
		return createSuccess;
	}
	
	/**
	 * Helper method that handles loading the configuration file for the
	 * application.
	 */
	private void loadDeploymentDescriptor() throws IOException {
		final InputStream is = ClassLoader.getSystemResourceAsStream(INI_FILE_PATH);
		if( is == null ) {
			String msg = "console.xml could not be found. Make sure it exists in the CONSOLE-INI directory.";
			throw new InitializerException(msg);
		}
		try {
			DeploymentDescriptorParser ddParser = new DeploymentDescriptorParser(is);
			ddParser.parse();
			applicationTitle = ddParser.getTitle();
			promptText = ddParser.getPrompt();
			exeConfig = ddParser.getExecutorConfig();
			inputParser = ddParser.getInputParser();
			listeners.addAll(ddParser.getListeners());
			cmdLineArgDefs.addAll(ddParser.getValidCommandLineArgs());
			SUDOERS.addAll(ddParser.getSudoerCreds());
			// getInFilters
			// getOutFilters
		}
		finally {
			is.close();
		}
	}
	
	/**
	 * Helper method that handles loading the console wide properties.
	 */
	private void loadGlobalProperties() throws IOException {
		final InputStream is = ClassLoader.getSystemResourceAsStream(INI_PROP_FILE_PATH);
		if( is != null ) {
			try {
				INI_PROPS.load(is);
			}
			catch(IOException ioe) {
				String msg = "Problem loading console.properties.";
				throw new InitializerException(msg, ioe);
			}
			finally {
				is.close();
			}
		}
	}
	
	private AbstractCommandExecutor createExecutor(ExecutorConfig config) {
		try {
            Class<?> cmdClass = Class.forName(config.getClassName());
            Constructor<?> cmdCtr = cmdClass.getDeclaredConstructor();
            AbstractCommandExecutor executor = (AbstractCommandExecutor)cmdCtr.newInstance();
            return executor;
        }
		catch(NoSuchMethodException nsmEx) {
            throw new InitializerException(nsmEx);
        }
        catch(ClassNotFoundException cnfEx) {
            throw new InitializerException(cnfEx);
        }
        catch(IllegalAccessException iaEx) {
            throw new InitializerException(iaEx);
        }
        catch(InstantiationException iEx) {
            throw new InitializerException(iEx);
        }
        catch(InvocationTargetException itEx) {
            throw new InitializerException(itEx);
        }
	}
	
}
