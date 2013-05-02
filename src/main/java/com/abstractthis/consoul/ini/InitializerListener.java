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
