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

import com.abstractthis.consoul.ConsoleProperties;

public class AboutWidget implements Widget {
	private final String appNameInfo;
	private final String appVersionInfo;
	
	public AboutWidget(String name, String version) {
		if( name == null ) name = "";
		if( version == null ) version = "";
		this.appNameInfo = " Application: " + name + " ";
		this.appVersionInfo = " Version: " + version + " ";
	}
	
	public AboutWidget(ConsoleProperties props) {
		this(props.getApplicationTitle(), props.getProperty("app.version"));
	}

	public void render(PrintStream stream) {
		int nameLength = appNameInfo.length();
		int versionLength = appVersionInfo.length();
		StringBuilder sb;
		if( nameLength > versionLength ) {
			// plus 3 for the bookend chars and the '\n'
			sb = new StringBuilder((nameLength + 3) * 4);
			String headerFooter = this.buildHeaderFooter(sb, nameLength);
			this.addNameInfo(sb, nameLength);
			this.addVersionInfo(sb, nameLength);
			sb.append(headerFooter);
		}
		else {
			sb = new StringBuilder((versionLength + 3) * 4);
			String headerFooter = this.buildHeaderFooter(sb, versionLength);
			this.addNameInfo(sb, versionLength);
			this.addVersionInfo(sb, versionLength);
			sb.append(headerFooter);
		}
		stream.print(sb.toString());
	}
	
	private String buildHeaderFooter(StringBuilder content, int len) {
		content.append('+');
		while( len > 0 ) {
			content.append('-');
			len--;
		}
		content.append('+')
		       .append('\n');
		return content.toString();
	}
	
	private void addNameInfo(StringBuilder content, int len) {
		content.append('|');
		content.append(this.appNameInfo);
		int infoLen = this.appNameInfo.length();
		if( infoLen < len) {
			int spaces = len - infoLen;
			while( spaces > 0 ) {
				content.append(' ');
				spaces--;
			}
		}
		content.append('|')
		       .append('\n');
	}
	
	private void addVersionInfo(StringBuilder content, int len) {
		content.append('|');
		content.append(this.appVersionInfo);
		int infoLen = this.appVersionInfo.length();
		if( infoLen < len) {
			int spaces = len - infoLen;
			while( spaces > 0 ) {
				content.append(' ');
				spaces--;
			}
		}
		content.append('|')
		       .append('\n');
	}

}
