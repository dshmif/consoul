package com.abstractthis.consoul.commands;

public final class CommandDefinition {
	
	private final String name;
	private final String implClazzName;
	private final boolean isSecure;
	private final boolean isContextAware;
	
	public CommandDefinition(String name, String clazzName, boolean isSecure, boolean isContextAware) {
		this.name = name;
		this.implClazzName = clazzName;
		this.isSecure = isSecure;
		this.isContextAware = isContextAware;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final String getImplClassName() {
		return this.implClazzName;
	}
	
	public final boolean isContextAware() {
		return this.isContextAware;
	}
	
	public final boolean isSecure() {
		return this.isSecure;
	}

}
