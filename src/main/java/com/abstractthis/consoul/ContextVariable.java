package com.abstractthis.consoul;

import java.util.concurrent.atomic.AtomicReference;


public class ContextVariable<T> {
	private AtomicReference<T> varRef;
	
	public ContextVariable() {
		// Default creates an empty variable
	}
	
	public ContextVariable(T t) {
		this.setContent(t);
	}
	
	public void clear() {
		varRef.set(null);
	}
	
	public T getContent() {
		return varRef.get();
	}
	
	public void setContent(T t) {
		varRef.set(t);
	}

}
