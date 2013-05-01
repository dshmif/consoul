package com.abstractthis.consoul.commands;

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
