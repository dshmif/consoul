package com.abstractthis.consoul.commands;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.abstractthis.consoul.ini.xml.ContentHandlerFactory;

final class CommandsConfigParser {
	
	private final Map<String, CommandDefinition> commands = new HashMap<String, CommandDefinition>();

	public CommandsConfigParser(InputStream validationStream, InputStream parseStream) {
		this.validate(validationStream);
		this.parse(parseStream);
	}
	
	public Map<String,CommandDefinition> getCommandsMap() {
		// Shallow copy is fine since we're dealing
		// with immutable classes in the Map.
		return new HashMap<String,CommandDefinition>(commands);
	}
	
	private void parse(InputStream is) {
		try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            XMLReader xmlReader = spf.newSAXParser().getXMLReader();
            ContentHandler cmdHandler = ContentHandlerFactory.getCommandDescriptorHandler(commands);
            xmlReader.setContentHandler(cmdHandler);
            xmlReader.parse(new InputSource(is));
        }
        catch(IOException e) {
            throw new RuntimeException("IO issue parsing command descriptor", e);
        }
        catch(SAXException e) {
            throw new RuntimeException("SAX issue parsing command descriptor", e);
        }
        catch(ParserConfigurationException e) {
            throw new RuntimeException("Parser Config issue with command descriptor", e);
        }
	}
	
	private void validate(InputStream is) {
		
	}
	
}
