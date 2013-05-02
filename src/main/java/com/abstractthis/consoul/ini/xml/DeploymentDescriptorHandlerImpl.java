package com.abstractthis.consoul.ini.xml;

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

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.XMLFilterImpl;

import com.abstractthis.consoul.ini.CommandLineArgDef;
import com.abstractthis.consoul.ini.ExecutorConfig;

final class DeploymentDescriptorHandlerImpl extends XMLFilterImpl implements DeploymentDescriptorHandler {
	
	/* Artifacts after a successful parsing of the DD */
	private String appTitle;
	private String prompt;
	private ExecutorConfig exeConfig;
	private List<CommandLineArgDef> cmdArgDefs = new ArrayList<CommandLineArgDef>();
	private List<String> filterClassNames = new ArrayList<String>();
	private List<String> listenerClassNames = new ArrayList<String>();
	private List<String[]> sudoerCreds = new ArrayList<String[]>();
	private String cmdLineParserClassName;
	
	/* Processing flags */
	private boolean processDD;
	private boolean processTitle;
	private boolean processPrompt;
	private boolean processExecutor;
	private boolean processExeClass;
	private boolean processCmdConfig;
	private boolean processFilter;
	private boolean processListener;
	private boolean processCmdLineParser;
	private boolean processingError;
	private boolean processSudoer;
	
	/* Temporary data holders */
	private String exeClassName;
	private String exeCmdConfig;
	private String filterClassName;
	private String listenerClassName;
	
	public List<CommandLineArgDef> getDescribedCommandLineArgs() {
		return this.cmdArgDefs;
	}
	
	public ExecutorConfig getDescribedExecutor() {
		return this.exeConfig;
	}
	
	public String getDescribedPrompt() {
		return this.prompt == null ? "" : this.prompt;
	}
	
	public String getDescribedTitle() {
		return this.appTitle;
	}
	
	public String getCommandLineParserClassName() {
		return this.cmdLineParserClassName;
	}
	
	public List<String> getFilterClassNames() {
		return this.filterClassNames;
	}
	
	public List<String> getListenerClassNames() {
		return this.listenerClassNames;
	}
	
	public List<String[]> getSudoerCreds() {
		return this.sudoerCreds;
	}
	
	public boolean isProcessingError() {
		return this.processingError;
	}
	
	/**
     * Called when the parser gets to a element open tag.
     * @param uri
     * @param localName
     * @param qName
     * @param atts
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) {
        if( "console-app".equals(localName) ) {
        	processDD = true;
        }
        else if( "title".equals(localName) ) {
        	processTitle = true;
        }
        else if( "prompt".equals(localName) ) {
        	processPrompt = true;
        }
        else if( "listener".equals(localName) ) {
        	processListener = true;
        }
        else if( "filter".equals(localName) ) {
        	processFilter = true;
        }
        else if( "executor".equals(localName) ) {
        	processExecutor = true;
        }
        else if( "exe-class".equals(localName) ) {
        	processExeClass = true;
        }
        else if( "command-config".equals(localName) ) {
        	processCmdConfig = true;
        }
        else if( "cmdline-parser".equals(localName) ) {
        	processCmdLineParser = true;
        }
        else if( "sudoer".equals(localName) ) {
        	processSudoer = true;
        	String sudoerUser = atts.getValue(uri, "user");
        	String sudoerPwd = atts.getValue(uri, "pwd");
        	this.sudoerCreds.add(new String[] {sudoerUser, sudoerPwd});
        	processSudoer = false;
        }
    }
	
	/**
     * Called when the parser grabs the characters between the
     * element tags.
     * @param chars
     * @param start
     * @param length
     */
    @Override
    public void characters(char[] chars, int start, int length) {
    	if( !processDD ) return;
    	String elementValue = new String(chars, start, length);
    	if( processTitle ) {
    		this.appTitle = elementValue;
    	}
    	else if( processPrompt ) {
    		this.prompt = elementValue;
    	}
    	else if( processListener ) {
    		this.listenerClassName = elementValue;
    	}
    	else if( processFilter ) {
    		this.filterClassName = elementValue;
    	}
    	else if( processExecutor ) {
    		if( processExeClass ) {
    			this.exeClassName = elementValue;
    		}
    		else if( processCmdConfig ) {
    			this.exeCmdConfig = elementValue;
    		}
    	}
    	else if( processCmdLineParser ) {
    		this.cmdLineParserClassName = elementValue;
    	}
    	else if( processSudoer ) {
    		// Empty element
    	}
    }

    /**
     * Called when the parser gets to a element close tag.
     * @param uri
     * @param localName
     * @param qName
     */
    @Override
    public void endElement(String uri, String localName, String qName) {
    	if( "console-app".equals(localName) ) {
    		if( !this.processSuccessful() ) {
    			processingError = true;
    		}
    		processDD = false;
        }
        else if( "title".equals(localName) ) {
        	processTitle = false;
        }
        else if( "prompt".equals(localName) ) {
        	processPrompt = false;
        }
        else if( "listener".equals(localName) ) {
        	if( listenerClassName != null ) {
        		this.listenerClassNames.add(listenerClassName);
        		listenerClassName = null;
        	}
        	processListener = false;
        }
        else if( "filter".equals(localName) ) {
        	if( filterClassName != null ) {
        		this.filterClassNames.add(filterClassName);
        		filterClassName = null;
        	}
        	processFilter = false;
        }
        else if( "executor".equals(localName) ) {
        	if( exeClassName == null || exeCmdConfig == null ) {
        		processingError = true;
        	}
        	else {
        		exeConfig = new ExecutorConfig(exeClassName, exeCmdConfig);
        	}
        	processExecutor = false;
        }
        else if( "exe-class".equals(localName) ) {
        	processExeClass = false;
        }
        else if( "command-config".equals(localName) ) {
        	processCmdConfig = false;
        }
        else if( "cmdline-parser".equals(localName) ) {
        	processCmdLineParser = false;
        }
        else if( "sudoer".equals(localName) ) {
        	processSudoer = false;
        }
    }
    
    private boolean processSuccessful() {
    	return this.filterClassName == null &&
    	       this.listenerClassName == null &&
    	       this.exeConfig != null && 
    	       this.appTitle != null;
    }
    
}
