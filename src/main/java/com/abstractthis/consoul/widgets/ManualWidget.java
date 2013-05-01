package com.abstractthis.consoul.widgets;

import java.io.PrintStream;


public class ManualWidget implements Widget {
	private final static String footer = "################################################################################";
	private final String cmdName;
	private final String content;
	
	public ManualWidget(String cmdName, String content) {
		this.cmdName = cmdName;
		this.content = content;
	}

	public void render(PrintStream stream) {
		stream.println();
		stream.println(this.buildHeader());
		stream.println(this.content);
		stream.println();
		stream.println(footer);
	}
	
	private String buildHeader() {
		final int HEADER_LENGTH = 80;
		String title = " " + this.cmdName + " Manual ";
		int charsUsed = HEADER_LENGTH - title.length();
		int hashCnt = charsUsed / 2;
		StringBuilder sb = new StringBuilder(80);
		for(int i = hashCnt; i > 0; i--) {
			sb.append('#');
		}
		sb.append(title);
		int headerLength = sb.length();
		while( headerLength < 80 ) {
			sb.append('#');
			headerLength++;
		}
		return sb.toString();
	}

}
