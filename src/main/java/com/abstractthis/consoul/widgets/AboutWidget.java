package com.abstractthis.consoul.widgets;

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

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.abstractthis.consoul.ConsoleProperties;

public class AboutWidget implements Widget {
	private final String appNameInfo;
	private final String appVersionInfo;
	private final Map<String,String> info;
	
	public AboutWidget(String name, String version) {
		if( name == null ) name = "";
		if( version == null ) version = "";
		this.appNameInfo = "Application: " + name;
		this.appVersionInfo = "Version: " + version;
		info = null;
	}
	
	public AboutWidget(ConsoleProperties props) {
		this(props.getApplicationTitle(), props.getProperty("app.version"));
	}
	
	public AboutWidget(Map<String,String> aboutInfo) {
		this.appNameInfo = null;
		this.appVersionInfo = null;
		this.info = new HashMap<String,String>(aboutInfo);
	}

	public void render(PrintStream stream) {
		String[] content = this.info == null ?
				new String[] {this.appNameInfo, this.appVersionInfo} :
					this.flattenMapInfo(this.info); 
		int contentLength =
				this.determineContentWidth(content);
		final int size = this.getBufferSize(contentLength);
		StringBuilder contentBuffer = new StringBuilder(size);
		String headerFooter = this.buildHeaderFooter(contentBuffer, contentLength);
		for(String element : content) {
			this.addContentElement(element, contentBuffer, contentLength);
		}
		contentBuffer.append(headerFooter);
		stream.print(contentBuffer.toString());
	}
	
	private String[] flattenMapInfo(Map<String,String> content) {
		Set<Map.Entry<String,String>> contentSet = this.info.entrySet();
		String[] flatContent = new String[contentSet.size()];
		int i = 0;
		for(Map.Entry<String, String> e : contentSet) {
			String s = String.format("%s: %s", e.getKey(), e.getValue());
			flatContent[i++] = s;
		}
		return flatContent;
	}
	
	private int determineContentWidth(String[] content) {
		int width = 0;
		for(String s : content) {
			int strLength = s.length();
			if( strLength > width ) {
				width = strLength;
			}
		}
		return width;
	}
	
	private int getBufferSize(int contentLength) {
		// plus 5 for the bookend chars and the '\n'
		return (contentLength + 5) * 4;
	}
	
	/*
	 * Odd helper method in that it adds the header to the content but
	 * also returns the same header so that it can be used, without having
	 * to rebuild it, as the footer. 
	 * @param content
	 * @param len
	 * @return
	 */
	private String buildHeaderFooter(StringBuilder content, int len) {
		// Add 2 to the length for the bookend spaces for each content element
		len += 2;
		content.append('+');
		while( len > 0 ) {
			content.append('-');
			len--;
		}
		content.append('+')
		       .append('\n');
		return content.toString();
	}
	
	private void addContentElement(String element, StringBuilder content, int len) {
		content.append('|').append(' ').append(element).append(' ');
		int elemLen = element.length();
		if( elemLen < len) {
			int spaces = len - elemLen;
			while( spaces > 0 ) {
				content.append(' ');
				spaces--;
			}
		}
		content.append('|').append('\n');
	}

}
