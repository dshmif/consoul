package com.abstractthis.consoul;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import com.abstractthis.consoul.commands.AbstractCommandExecutor;
import com.abstractthis.consoul.commands.ConsoleCommand;
import com.abstractthis.consoul.ini.ConsoleInitializer;
import com.abstractthis.consoul.ini.InitializerException;

public final class ConsoleApplication {
	private final ExecutorService cmdExeService = Executors.newSingleThreadExecutor();
	private final ApplicationContext context = new ApplicationContext();
	private ConsoleDisplay display;
	private AbstractCommandExecutor cmdExecutor;
	private volatile boolean shutdown;
	
	/**
	 * Private constructor which is called by the factor method
	 * so that <code>this</code> is not leaked.
	 * @param args the command line arguments
	 */
	private ConsoleApplication(String[] args) {
		this.initApplication(args);
	}
	
	/**
	 * Called when the <code>ConsoleApplication</code> is constructed.
	 * @param args the command line arguments
	 */
	private void initApplication(String[] args) {
		try {
			this.registerShutdownHook();
			ConsoleInitializer consoleIniter = ConsoleInitializer.getInitializer();
			consoleIniter.initialize(args);
			this.initApplicationContext();
			this.cmdExecutor = consoleIniter.getCommandExecutor();
			display = new ConsoleDisplay(this, consoleIniter.getInputParser());
			display.setPromptText(consoleIniter.getPromptText());
			consoleIniter.runInitializers(display.getNewOutPipe());
			String appTitle = consoleIniter.getApplicationTitle();
			display.render(new WelcomeWidget(appTitle));
			display.render(new PromptWidget(display));
			synchronized( this ) {
				if( !shutdown ) {
					try { this.wait(); }
					catch(InterruptedException ignored) {
						// Application is shutting down just let it go
					}
				}
			}
		}
		catch(InitializerException ie) {
			System.out.println("Issues Initializing the Consoul platform...");
			ie.printStackTrace();
		}
	}
	
	private void initApplicationContext() {
		this.buildAndStoreSudoersSet();
	}
	
	private void buildAndStoreSudoersSet() {
		String sudoerCSHashes = this.getSudoerHashes();
		String[] sudoerHashes = sudoerCSHashes.split(",");
		Set<String> sudoersHashSet = new HashSet<String>(sudoerHashes.length);
		for(String hash : sudoerHashes) {
			if ( hash != null && !"".equals(hash) ) {
				sudoersHashSet.add(hash);
			}
		}
		// Place the sudoer hashes in the Application Context for use
		// by the SecureCommands
		ContextVariable<Set<String>> cxtSudoerHashes =
				new ContextVariable<Set<String>>(sudoersHashSet);
		context.put(ConsoleInitializer.getInitializer().getSudoersPropKey(), cxtSudoerHashes);
	}
	
	private String getSudoerHashes() {
		final String SUDOER_FILE_PATH = ConsoleInitializer.getInitializer().getSudoersPath();
		final InputStream is = ClassLoader.getSystemResourceAsStream(SUDOER_FILE_PATH);
		if( is != null ) {
			final Properties sudoers = new Properties();
			try {
				sudoers.load(is);
				return sudoers.getProperty(ConsoleInitializer.getInitializer().getSudoersPropKey());
			}
			catch(IOException ioe) {
				throw new InitializerException(ioe);
			}
			finally {
				try { is.close(); }
				catch(IOException ioe2) { /* NOP */}
			}
		}
		return "";
	}
	
	/**
	 * Handles performing all cleanup and shutting down of services.
	 */
	private void registerShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if( !ConsoleApplication.this.shutdown ) {
					this.shutdownCleanup();
				}
			}
			
			private void shutdownCleanup() {
				try {
					ConsoleApplication.this.shutdown = true;
					ConsoleInitializer.getInitializer().deinitialize(display.getNewOutPipe());
					display.detach();
					cmdExeService.shutdown();
					cmdExeService.awaitTermination(5000, TimeUnit.MILLISECONDS);
				}
				catch(InterruptedException ie) {
					// Application is shutting down so just let it go
				}
			}
		});
	}
	
	/**
	 * Factory method that creates a new instance of <code>ConsoleApplication</code>.
	 * The full initialization of the application is triggered by calling
	 * this method. The intent is for this to be called once.
	 * @param args the command line arguments
	 * @return a fully initialized console application
	 */
	public static ConsoleApplication launchNativeApplication(String[] args) {
		return new ConsoleApplication(args);
	}
	
	/**
	 * Submits the specified <code>ConsoleCommand</code> for processing.
	 * @param command the command requested for execution by the user
	 */
	public void executeCommand(ConsoleCommand command) {
		if( this.isQuitCommand(command) ) {
			display.render("Shutting down application....");
			this.shutdownApplication();
		}
		else {
			try {
				cmdExeService.submit(
						new CommandExeTask(context,
								           this.display,
								           this.cmdExecutor,
								           command));
			}
			catch(RejectedExecutionException ignored) {
				display.render("Execution of command rejected.");
			}
		}
	}
	
	/**
	 * Determines whether the command input to the console is
	 * the command for shutting down the application.
	 * <p>
	 * Both <code>quit</code> and <code>exit</code> are reserved
	 * and are interpreted as requests to shutdown.
	 * @param command
	 * @return true if the command is a shutdown command
	 */
	private boolean isQuitCommand(ConsoleCommand command) {
		String cmdName = command.getCommandName();
		return "quit".equalsIgnoreCase(cmdName) ||
		       "exit".equalsIgnoreCase(cmdName);
	}
	
	/**
	 * Performs application cleanup so that the JVM can shutdown.
	 */
	private void shutdownApplication() {
		// The registered shutdown hook will handle cleanup
		System.exit(0);
	}
	
	/**
	 * Inner class represents the task of executing a command entered into the console.
	 * Currently the only types of commands supported are synchronous commands. Therefore
	 * only a single command is executed at a time. Once the command is done executing
	 * the console input is listened to again for input.
	 * @author David Smith
	 *
	 */
	private static class CommandExeTask implements Callable<Void> {
		private final ApplicationContext context;
		private final ConsoleDisplay display;
		private final AbstractCommandExecutor cmdExecutor;
		private final ConsoleCommand command;
		
		public CommandExeTask(ApplicationContext cxt, ConsoleDisplay display, AbstractCommandExecutor executor, ConsoleCommand cmd) {
			this.context = cxt;
			this.display = display;
			this.cmdExecutor = executor;
			this.command = cmd;
		}
		
		public Void call() throws Exception {
			command.setApplicationContext(context);
			command.setCommandOutputPipe(display.getNewOutPipe());
			cmdExecutor.processCommand(command);
			display.render(new PromptWidget(display));
			return null;
		}
	}

}
