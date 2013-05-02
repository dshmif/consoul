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

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.abstractthis.consoul.commands.CommandCreds;
import com.abstractthis.consoul.ini.ConsoleInitializer;
import com.abstractthis.consoul.util.BCrypt;

public final class ApplicationContext extends ConcurrentHashMap<String, ContextVariable<?>> {
	private static final long serialVersionUID = 6433575245256456889L;
	
	private static final String CURRENT_SUDO_COMMAND = "command.sudo";
	private static final String SUDOERS_CONTEXT_KEY = ConsoleInitializer.getInitializer().getSudoersPropKey();

	public ApplicationContext() {
		super();
	}
	
	public ApplicationContext(int initialCapacity) {
		super(initialCapacity);
	}
	
	public ApplicationContext(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}
	
	public ApplicationContext(int initialCapacity, float loadFactor, int concurrencyLevel) {
		super(initialCapacity, loadFactor, concurrencyLevel);
	}
	
	public boolean isSudoer() {
		CommandCreds creds = this.removeCurrentSudoer();
		return this.isSudoer(creds);
	}
	
	public boolean isSudoer(CommandCreds creds) {
		return this.isSudoer(creds.getCredsForHashing());
	}
	
	public boolean isSudoer(String user, String password) {
		return this.isSudoer(user + password);
	}
	
	public boolean isSudoer(String concatCred) {
		@SuppressWarnings("unchecked")
		ContextVariable<Set<String>> sudoersCxtVar = (ContextVariable<Set<String>>) this.get(SUDOERS_CONTEXT_KEY);
		Set<String> sudoersSet = sudoersCxtVar.getContent();
		for(Iterator<String> i = sudoersSet.iterator(); i.hasNext();) {
			String hash = i.next();
			if( BCrypt.checkpw(concatCred, hash) ) {
				return true;
			}
		}
		return false;
	}
	
	public void setCurrentSudoer(CommandCreds creds) {
		@SuppressWarnings("unchecked")
		ContextVariable<CommandCreds> ctxtCreds =
				(ContextVariable<CommandCreds>) this.remove(CURRENT_SUDO_COMMAND);
		if( ctxtCreds != null ) {
			ctxtCreds.clear();
			ctxtCreds = null;
		}
		ctxtCreds = new ContextVariable<CommandCreds>(creds);
		this.put(CURRENT_SUDO_COMMAND, ctxtCreds);
		System.err.println("Current Sudoer set");
	}
	
	public CommandCreds removeCurrentSudoer() {
		@SuppressWarnings("unchecked")
		ContextVariable<CommandCreds> ctxtCreds =
				(ContextVariable<CommandCreds>)this.remove(CURRENT_SUDO_COMMAND);
		return ctxtCreds.getContent();
	}
	
}