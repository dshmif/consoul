package com.abstractthis.consoul;

import java.io.PrintStream;

import com.abstractthis.consoul.widgets.Widget;

final class WelcomeWidget implements Widget {
	private final String appTitle;
	
	public WelcomeWidget(String title) {
		this.appTitle = title;
	}

	public void render(PrintStream stream) {
		StringBuilder sb = new StringBuilder(64);
		int hashCharCount = this.appTitle.length() + 10;
		if( hashCharCount > 80 ) hashCharCount = 80;
		for(int i = hashCharCount; i > 0; i--) {
			sb.append("#");
		}
		String hashStr = sb.toString();
		sb.delete(0, sb.length());
		int spaceCnt = hashCharCount - (this.appTitle.length() + 2);
		sb.append("#");
		for(int i = spaceCnt/2; i >0; i--) {
			sb.append(" ");
		}
		sb.append(this.appTitle);
		for(int i = spaceCnt/2; i >0; i--) {
			sb.append(" ");
		}
		sb.append("#");
		stream.println(hashStr);
		stream.println(sb.toString());
		stream.println(hashStr);
		stream.println();
	}

}
