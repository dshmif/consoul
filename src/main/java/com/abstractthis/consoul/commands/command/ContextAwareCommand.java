package com.abstractthis.consoul.commands.command;

import com.abstractthis.consoul.ApplicationContext;

public interface ContextAwareCommand extends Command {

	public ApplicationContext getContext();
	public void setContext(ApplicationContext context);
	
}
