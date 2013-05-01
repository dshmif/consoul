package com.abstractthis.consoul.ini;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.abstractthis.consoul.ConsoleInputParser;
import com.abstractthis.consoul.filter.ConsoleFilter;
import com.abstractthis.consoul.ini.xml.ContentHandlerFactory;
import com.abstractthis.consoul.ini.xml.DeploymentDescriptorHandler;

class DeploymentDescriptorParser {
	private InputStream streamToDD;
	private DeploymentDescriptorHandler ddHandler;
	private List<InitializerListener> listeners;
	private List<ConsoleFilter> filters;
	private List<CommandLineArgDef> validArgs;
	private ConsoleInputParser inputParser;
	
	public DeploymentDescriptorParser(InputStream is) {
		this.streamToDD = is;
	}
	
	public ExecutorConfig getExecutorConfig() {
		return ddHandler.getDescribedExecutor();
	}
	
	public ConsoleInputParser getInputParser() {
		return this.inputParser;
	}
	
	public List<InitializerListener> getListeners() {
		return this.listeners;
	}
	
	public List<ConsoleFilter> getFilters() {
		return this.filters;
	}
	
	public String getPrompt() {
		return ddHandler.getDescribedPrompt();
	}
	
	public List<String[]> getSudoerCreds() {
		return ddHandler.getSudoerCreds();
	}
	
	public String getTitle() {
		return ddHandler.getDescribedTitle();
	}
	
	public List<CommandLineArgDef> getValidCommandLineArgs() {
		return this.validArgs;
	}
	
	public void parse() {
		try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            XMLReader xmlReader = spf.newSAXParser().getXMLReader();
            ddHandler = ContentHandlerFactory.getDeploymentDescriptorHandler();
            xmlReader.setContentHandler((ContentHandler)ddHandler);
            xmlReader.parse(new InputSource(streamToDD));
            if( !ddHandler.isProcessingError() ) {
            	this.buildDDComponents();
            }
            else {
            	throw new InitializerException("Error processing Deployment Descriptor.");
            }
        }
        catch(IOException e) {
            throw new InitializerException("IO issue parsing deployment descriptor", e);
        }
        catch(SAXException e) {
            throw new InitializerException("SAX issue parsing deployment descriptor", e);
        }
        catch(ParserConfigurationException e) {
            throw new InitializerException("Parser Config issue with deployment descriptor", e);
        }
	}
	
	private void buildDDComponents() {
		List<String> filterClassNames = ddHandler.getFilterClassNames();
		this.buildFilters(filterClassNames);
		List<String> listenerClassNames = ddHandler.getListenerClassNames();
		this.buildListeners(listenerClassNames);
		String inputParserClassName = ddHandler.getCommandLineParserClassName();
		this.buildConsoleInputParser(inputParserClassName);
		this.buildValidCommandLineArgs();
	}
	
	private void buildFilters(List<String> classNames) {
		if( classNames.isEmpty() ) {
			this.filters = Collections.emptyList();
		}
		else {
			List<ConsoleFilter> tempList = new ArrayList<ConsoleFilter>(classNames.size());
			try {
				for(String className : classNames) {
		            Class<?> filterClass = Class.forName(className);
		            ConsoleFilter filter = (ConsoleFilter)filterClass.newInstance();
		            tempList.add(filter);
				}
				this.filters = new ArrayList<ConsoleFilter>(tempList);
	        }
	        catch(ClassNotFoundException cnfEx) {
	            throw new InitializerException(cnfEx);
	        }
	        catch(IllegalAccessException iaEx) {
	            throw new InitializerException(iaEx);
	        }
	        catch(InstantiationException iEx) {
	            throw new InitializerException(iEx);
	        }
		}
	}
	
	private void buildListeners(List<String> classNames) {
		if( classNames.isEmpty() ) {
			this.listeners = Collections.emptyList();
		}
		else {
			List<InitializerListener> tempList = new ArrayList<InitializerListener>(classNames.size());
			try {
				for(String className : classNames) {
		            Class<?> listenerClass = Class.forName(className);
		            InitializerListener listener = (InitializerListener)listenerClass.newInstance();
		            tempList.add(listener);
				}
				this.listeners = new ArrayList<InitializerListener>(tempList);
	        }
	        catch(ClassNotFoundException cnfEx) {
	            throw new InitializerException(cnfEx);
	        }
	        catch(IllegalAccessException iaEx) {
	            throw new InitializerException(iaEx);
	        }
	        catch(InstantiationException iEx) {
	            throw new InitializerException(iEx);
	        }
		}
	}
	
	private void buildConsoleInputParser(String className) {
		if( className != null ) {
			try {
	            Class<?> parserClass = Class.forName(className);
	            this.inputParser = (ConsoleInputParser)parserClass.newInstance();
	        }
	        catch(ClassNotFoundException cnfEx) {
	            throw new InitializerException(cnfEx);
	        }
	        catch(IllegalAccessException iaEx) {
	            throw new InitializerException(iaEx);
	        }
	        catch(InstantiationException iEx) {
	            throw new InitializerException(iEx);
	        }
		}
	}
	
	private void buildValidCommandLineArgs() {
		// Functionality not supported yet. Just create
		// an empty collection.
		this.validArgs = Collections.emptyList();
	}
	
//	private void validate() {
//		
//	}

}
