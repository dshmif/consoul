package com.abstractthis.consoul;

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
