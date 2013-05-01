package com.abstractthis.consoul.widgets;

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
