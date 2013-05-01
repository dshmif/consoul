package com.abstractthis.consoul.ini;

import com.abstractthis.consoul.ConsoleOutPipe;


/**
 * Allows for the objects that are responsible for initializing
 * the system to be able to listen to when they should perform
 * their initialization tasks and deinitialization tasks.
 * 
 * @author David Smith
 *
 */
public interface InitializerListener {

	/**
	 * Called by the <code>ConsoleInitializer</code> when the
	 * console application is initially created.
	 */
	public void initialize(ConsoleOutPipe outPipe);
	
	/**
	 * Called by the <code>ConsoleInitializer</code> when the
	 * console application is shutdown.
	 */
	public void deinitialize(ConsoleOutPipe outPipe);
	
}
