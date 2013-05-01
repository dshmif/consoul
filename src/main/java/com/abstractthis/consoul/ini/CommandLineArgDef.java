package com.abstractthis.consoul.ini;

public final class CommandLineArgDef {
	private final int argPosition;
	private final String argName;
	private final boolean isFlag;
	private final boolean isRequired;
	
	public CommandLineArgDef(int position, String name, boolean isFlag, boolean isRequired) {
		this.argPosition = position;
		this.argName = name;
		this.isFlag = isFlag;
		this.isRequired = isRequired;
	}
	
	/**
	 * Returns the name associated with this argument definition.
	 * <p>
	 * Example of use would be an an 'USAGE' statement.
	 * @return the name of this argument definition
	 */
	public final String getArgName() {
		return this.argName;
	}
	
	/**
	 * The position that a command line argument conforming to this
	 * definition must be located in.
	 * @return the position that an argument conforming to this
	 * argument definition must be located in
	 */
	public final int getArgPosition() {
		return this.argPosition;
	}
	
	/**
	 * Returns <code>true</code> if an argument that conforms to this
	 * definition is a flag (switch) argument.
	 * @return true if an argument conforming to this definition must
	 * be a switch argument
	 */
	public final boolean isFlag() {
		return this.isFlag;
	}
	
	/**
	 * Returns <code>true</code> if an argument that conforms to this
	 * definition is required to launch the console application.
	 * @return true if an argument conforming to this definition must
	 * be provided to launch the console application.
	 */
	public final boolean isRequired() {
		return this.isRequired;
	}
	
	/**
	 * Returns <code>true</code> if the provided command line
	 * argument conforms to this command line argument definition.
	 * @param arg the command line argument to validate
	 * @return true if the <code>arg</code> conforms to this definition
	 */
	public final boolean valid(CommandLineArg arg) {
		CommandLineArgDef argDef = arg.getCommandLineArgDef();
		return argDef.getArgPosition() == this.argPosition &&
		       argDef.isFlag() == this.isFlag;
	}
	
}
