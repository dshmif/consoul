package com.abstractthis.consoul.ini;

public final class CommandLineArg {
	private final CommandLineArgDef definition;
	private final String argument;
	
	public CommandLineArg(CommandLineArgDef def, String argument) {
		this.definition = def;
		this.argument = argument;
	}
	
	public CommandLineArg(int position, String name, boolean isFlag, boolean isRequired, String argument) {
		this(new CommandLineArgDef(position, name, isFlag, isRequired), argument);
	}
	
	public final String getArgument() {
		return this.argument;
	}
	
	public final String getArgumentName() {
		return this.definition.getArgName();
	}
	
	public final int getArgumentPosition() {
		return this.definition.getArgPosition();
	}
	
	public final CommandLineArgDef getCommandLineArgDef() {
		return this.definition;
	}
	
	public final boolean isFlag() {
		return this.definition.isFlag();
	}
	
}
