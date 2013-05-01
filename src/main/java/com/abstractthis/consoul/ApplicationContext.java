package com.abstractthis.consoul;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
	
	@SuppressWarnings("unchecked")
	public boolean isSudoer() {
		ContextVariable<Set<String>> sudoersCxtVar = (ContextVariable<Set<String>>) this.get(SUDOERS_CONTEXT_KEY);
		Set<String> sudoersSet = sudoersCxtVar.getContent();
		String hash = (String) this.get(CURRENT_SUDO_COMMAND).getContent();
		return sudoersSet.contains(hash);
	}
	
	public boolean isSudoer(String user, String password) {
		return this.isSudoer(user + password);
	}
	
	@SuppressWarnings("unchecked")
	public boolean isSudoer(String concatCred) {
		ContextVariable<Set<String>> sudoersCxtVar = (ContextVariable<Set<String>>) this.get(SUDOERS_CONTEXT_KEY);
		Set<String> sudoersSet = sudoersCxtVar.getContent();
		String hash = BCrypt.hashpw(concatCred, BCrypt.gensalt(12));
		return sudoersSet.contains(hash);
	}
	
	public void setCurrentSudoer(String hash) {
		ContextVariable<?> creds = this.remove(CURRENT_SUDO_COMMAND);
		if( creds != null ) {
			creds.clear();
			creds = null;
		}
		creds = new ContextVariable<String>(hash);
		this.put(CURRENT_SUDO_COMMAND, creds);
	}
	
	@SuppressWarnings("unchecked")
	public String removeCurrentSudoer() {
		ContextVariable<String> sudoHash = (ContextVariable<String>) this.get(CURRENT_SUDO_COMMAND);
		return sudoHash.getContent();
	}
	
}