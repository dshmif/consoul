package com.abstractthis.consoul.commands;

public final class CommandCreds {

	private final String username;
	private final String password;
	
	public CommandCreds(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public final String getPassword() {
		return this.password;
	}
	
	public final String getUsername() {
		return this.username;
	}
	
	public final String getCredsForHashing() {
		return this.username + this.password;
	}
}
