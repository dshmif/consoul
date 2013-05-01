package com.abstractthis.consoul.ini;

public final class ExecutorConfig {
	private final String commandConfigPath;
	private final String className;
	
	public ExecutorConfig(String className, String cmdConfigPath) {
		this.className = className;
		this.commandConfigPath = cmdConfigPath;
	}
	
	public final String getClassName() {
		return this.className;
	}
	
	public final String getCommandConfigPath() {
		return this.commandConfigPath;
	}
	
}
