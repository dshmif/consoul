package com.abstractthis.consoul.widgets;

import java.io.PrintStream;
import java.util.Scanner;

import com.abstractthis.consoul.ApplicationContext;
import com.abstractthis.consoul.util.BCrypt;

public class SudoWidget implements Widget {
	private static final String PROMPT_FOR_USERNAME = "User:";
	private static final String PROMPT_FOR_PASSWORD = "Password:";
	
	private ApplicationContext context;
	
	public SudoWidget(ApplicationContext context) {
		this.context = context;
	}

	public void render(PrintStream stream) {
		stream.print(PROMPT_FOR_USERNAME);
		Scanner inputScan = new Scanner(System.in);
		String username = inputScan.nextLine();
		stream.print(PROMPT_FOR_PASSWORD);
		String password = inputScan.nextLine();
		String echo = String.format("User: %s    Password: %s", username, password);
		String hash = BCrypt.hashpw(username + password, BCrypt.gensalt(12));
		context.setCurrentSudoer(hash);
		stream.print(echo);
	}

}
