package com.abstractthis.consoul;

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
