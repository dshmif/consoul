package com.abstractthis.consoul;

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
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import com.abstractthis.consoul.commands.ConsoleCommand;
import com.abstractthis.consoul.commands.DefaultConsoleCommand;
import com.abstractthis.consoul.widgets.Widget;

final class ConsoleDisplay {
	private final ExecutorService outputExec = Executors.newSingleThreadExecutor();
	private final ExecutorService inputExec = Executors.newSingleThreadExecutor();
	private final ConsoleApplication app;
	private final ConsoleInputParser inputParser;
	private String promptText;

	public ConsoleDisplay(ConsoleApplication app, ConsoleInputParser parser) {
		this.app = app;
		this.inputParser = parser == null ? new DefaultConsoleInputParser() : parser;
	}
	
	public ConsoleOutPipe getNewOutPipe() {
		return new ConsoleOutPipe(this);
	}
	
	public String getPromptText() {
		return this.promptText;
	}
	
	public void attach() {
		try {
			inputExec.submit(new ConsoleInputTask());
		}
		catch(RejectedExecutionException ignored) {
			System.out.println("attach task rejected.");
		}
	}
	
	public void detach() throws InterruptedException {
		inputExec.shutdownNow();
		outputExec.shutdown();
		outputExec.awaitTermination(2000, TimeUnit.MILLISECONDS);
	}
	
	public void render(String output) {
		try {
			outputExec.submit(new StringOutputTask(output));
		}
		catch(RejectedExecutionException ignored) {}
	}
	
	public void render(String output, boolean useNewline) {
		try {
			outputExec.submit(new StringOutputTask(output, useNewline));
		}
		catch(RejectedExecutionException ignored) {}
	}
	
	public void render(Widget widget) {
		try {
			outputExec.submit(new WidgetOutputTask(widget));
		}
		catch(RejectedExecutionException ignored) {}
	}
	
	public void render(String[] output) {
		try {
			List<String> outputList = Arrays.asList(output);
			outputExec.submit(new StringOutputTask(outputList));
		}
		catch(RejectedExecutionException ignored) {}
	}
	
	public void render(String[] output, boolean useNewline) {
		try {
			List<String> outputList = Arrays.asList(output);
			outputExec.submit(new StringOutputTask(outputList, useNewline));
		}
		catch(RejectedExecutionException ignored) {}
	}
	
	public void render(Widget[] widgets) {
		try {
			List<Widget> outputList = Arrays.asList(widgets);
			outputExec.submit(new WidgetOutputTask(outputList));
		}
		catch(RejectedExecutionException ignored) {}
	}
	
	public void setPromptText(String promptTxt) {
		this.promptText = promptTxt;
	}
	
	private class ConsoleInputTask implements Callable<Void> {
		public Void call() throws Exception {
			Scanner inputScan = new Scanner(System.in);
			String input = inputScan.nextLine();
			ConsoleCommand cmd = ConsoleDisplay.this.inputParser.parse(input);
			if( cmd != null ) ConsoleDisplay.this.app.executeCommand(cmd);
			else ConsoleDisplay.this.render(new PromptWidget(ConsoleDisplay.this));
			return null;
		}
	}
	
	private static class StringOutputTask implements Callable<Void> {
		private final List<String> multipleOutput;
		private final String output;
		private final boolean newline;
		
		public StringOutputTask(String outMsg) {
			this.output = outMsg;
			this.multipleOutput = null;
			this.newline = true;
		}
		
		public StringOutputTask(String outMsg, boolean useNewline) {
			this.output = outMsg;
			this.multipleOutput = null;
			this.newline = useNewline;
		}
		
		public StringOutputTask(List<String> outMsgs) {
			this.multipleOutput = new ArrayList<String>(outMsgs);
			this.output = null;
			this.newline = true;
		}
		
		public StringOutputTask(List<String> outMsgs, boolean useNewline) {
			this.multipleOutput = new ArrayList<String>(outMsgs);
			this.output = null;
			this.newline = useNewline;
		}
		
		public Void call() throws Exception {
			if( this.output == null ) {
				for(String outMsg : multipleOutput) {
					if( this.newline ) {
						System.out.println(outMsg);
					}
					else {
						System.out.print(outMsg);
					}
				}
			}
			else {
				if( this.newline ) {
					System.out.println(this.output);
				}
				else {
					System.out.print(this.output);
				}
			}
			return null;
		}
	}
	
	private static class WidgetOutputTask implements Callable<Void> {
		private final List<Widget> widgets;
		private final Widget widget;
		
		public WidgetOutputTask(Widget widget) {
			this.widget = widget;
			this.widgets = null;
		}
		
		public WidgetOutputTask(List<Widget> widgets) {
			this.widgets = widgets;
			this.widget = null;
		}
		
		public Void call() throws Exception {
			if( this.widget == null ) {
				for(Widget w : widgets) {
					w.render(System.out);
				}
			}
			else {
				widget.render(System.out);
			}
			return null;
		}
		
	}
	
	private class DefaultConsoleInputParser implements ConsoleInputParser {
		
		public ConsoleCommand parse(String cmdStr) {
			ConsoleCommand cc = null;
			if( cmdStr != null ) {
				cmdStr = cmdStr.trim();
				if( cmdStr.length() > 0 ) {
					String[] splitCmd = cmdStr.split(" ");
					if( splitCmd.length == 1 ) {
						cc = new DefaultConsoleCommand(splitCmd[0], new String[0]);
					}
					else {
						String[] params = this.copyParameters(splitCmd);
						cc = new DefaultConsoleCommand(splitCmd[0], params);
					}
				}
			}
			return cc;
		}
		
		private String[] copyParameters(String[] orig) {
			String[] copy = new String[orig.length - 1];
			for(int i = 0; i < copy.length; i++) {
				copy[i] = orig[i + 1];
			}
			return copy;
		}
		
	}
	
}
