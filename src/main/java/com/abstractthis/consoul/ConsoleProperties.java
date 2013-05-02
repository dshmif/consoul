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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import com.abstractthis.consoul.ini.ConsoleInitializer;

/**
 * Provides access to the property values specified in the console.properties
 * file in the mandatory CONSOLE-INI directory. Additionally it provides
 * access to the title of the application. The title is specified in the
 * deployment descriptor for the application.
 * 
 * @author David Smith
 */
public final class ConsoleProperties {
	private static final ConsoleProperties CONSOLE_PROPS = new ConsoleProperties();
	private final ConsoleInitializer initializer = ConsoleInitializer.getInitializer();
	
	// Private constructor enforces singleton property
	private ConsoleProperties() {}
	
	public static ConsoleProperties getConsoleProperties() {
		return CONSOLE_PROPS;
	}
	
	public String getApplicationTitle() {
		return initializer.getApplicationTitle();
	}
	
	public String getProperty(String name) {
		return initializer.getInitProperty(name);
	}
	
	public String[] getPropertyNames() {
		Enumeration<?> names = initializer.getInitPropertyNames();
		String[] propNamesArr = null;
		if( !names.hasMoreElements() ) {
			propNamesArr = new String[0];
		}
		else {
			List<String> propNames = new ArrayList<String>();
			while( names.hasMoreElements() ) {
				String name = (String)names.nextElement();
				propNames.add(name);
			}
			propNamesArr = propNames.toArray(new String[0]);
			Arrays.sort(propNamesArr);
		}
		return propNamesArr;
	}
	
}
