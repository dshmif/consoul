package com.abstractthis.consoul.ini.xml;

import java.util.Map;

import com.abstractthis.consoul.commands.CommandDefinition;

public abstract class ContentHandlerFactory {

	public static CommandDescriptorHandler getCommandDescriptorHandler(Map<String,CommandDefinition> cmdHolder) {
		return new CommandDescriptorHandler(cmdHolder);
	}
	
	public static DeploymentDescriptorHandler getDeploymentDescriptorHandler() {
		return new DeploymentDescriptorHandlerImpl();
	}
	
}
