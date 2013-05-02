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

import com.abstractthis.consoul.commands.ConsoleCommand;

/**
 * Allows for each application to determine how it wants to provide
 * the command line input into the application.
 * <p>
 * If a custom parser isn't provided in the <code>console.xml</code> application
 * deployment descriptor the default will be used. The default assumes the
 * following space-delimited input:
 * <p>
 * &lt;command&gt; &lt;parameter1&gt; &lt;parameter2&gt; ....
 * 
 * @author David Smith
 *
 */
public interface ConsoleInputParser {

	public ConsoleCommand parse(String cmdStr);
	
}
