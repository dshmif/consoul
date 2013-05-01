package com.abstractthis.consoul.ini.xml;

import java.util.List;

import com.abstractthis.consoul.ini.CommandLineArgDef;
import com.abstractthis.consoul.ini.ExecutorConfig;

public interface DeploymentDescriptorHandler {
	
	public String getCommandLineParserClassName();
	public String getDescribedTitle();
	public List<CommandLineArgDef> getDescribedCommandLineArgs();
	public ExecutorConfig getDescribedExecutor();
	public String getDescribedPrompt();
	public List<String> getFilterClassNames();
	public List<String> getListenerClassNames();
	public List<String[]> getSudoerCreds();
	public boolean isProcessingError();
	
}
