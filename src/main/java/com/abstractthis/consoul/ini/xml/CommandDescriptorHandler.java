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

import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.XMLFilterImpl;

import com.abstractthis.consoul.commands.CommandDefinition;

final class CommandDescriptorHandler extends XMLFilterImpl {
	
	/* Parsed Commands */
	private final Map<String,CommandDefinition> parsedCmds;
	
	/* Processing flags */
	private boolean processCommands;
	private boolean ignoreCurrentCommand;
	
	/* Command values */
	private String cmdName;
	private String cmdClassName;
	private boolean isSecure;
	private boolean isContextAware;
	
	/**
	 * Creates a new <code>CommandDescriptorHandler</code> that will
	 * populate the provided <code>Map</code> with the information
	 * obtained by parsing the commands descriptor file.
	 * @param cmdHolder <code>Map</code> that contains the information
	 * described for the commands in the descriptor file
	 */
	public CommandDescriptorHandler(Map<String,CommandDefinition> cmdHolder) {
		this.parsedCmds = cmdHolder;
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
    	if( "commands".equals(localName) ) {
    		processCommands = true;
        }
    	else if( "command".equals(localName) ) {
            if( processCommands ) {
                mapCommand(uri, atts);
            }
            else {
            	ignoreCurrentCommand = true;
            }
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
    	if( "commands".equals(localName) ) {
    		processCommands = false;
        }
    	else if( "command".equals(localName) ) {
    		if( !ignoreCurrentCommand ) {
	            // We don't want duplicates
	            if( !parsedCmds.containsKey(cmdName) ) {
	            	parsedCmds.put(cmdName, new CommandDefinition(cmdName, cmdClassName, isSecure, isContextAware));
	            }
	            // Reset all the cmd info so we can process the next one
	            cmdName = null;
	            cmdClassName = null;
	            isSecure = false;
	            isContextAware = false;
	            ignoreCurrentCommand = false;
    		}
        }
    }
    
    /**
     * Helper function that handles the command attributes.
     * @param atts attributes of the command being processed.
     */
    private void mapCommand(String uri, Attributes atts) {
        String attrCmdName = atts.getValue(uri, "name");
        String attrCmdClassName = atts.getValue(uri, "class");
        String attrSecure = atts.getValue(uri, "secure");
        String attrCtxtAware = atts.getValue(uri, "contextAware");
        // Make sure the two command attributes have values.
        if( (attrCmdName == null || "".equals(attrCmdName.trim())) ||
            (attrCmdClassName == null || "".equals(attrCmdClassName.trim())) ) {
        	ignoreCurrentCommand = true;
        }
        else {
        	cmdName = attrCmdName;
        	cmdClassName = attrCmdClassName;
        }
        
        // Grab the optional parameters that will just default to values
        // instead of causing the command to be ignored.
        if( attrSecure != null && !"".equals(attrSecure.trim()) ) {
        	attrSecure = attrSecure.trim();
        	isSecure = "true".equalsIgnoreCase(attrSecure);
        }
        if( attrCtxtAware != null && !"".equals(attrCtxtAware.trim()) ) {
        	attrCtxtAware = attrCtxtAware.trim();
        	isContextAware = "true".equalsIgnoreCase(attrCtxtAware);
        }
    }
    
}
