package com.abstractthis.consoul.ini;

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

public final class CommandLineArg {
	private final CommandLineArgDef definition;
	private final String argument;
	
	public CommandLineArg(CommandLineArgDef def, String argument) {
		this.definition = def;
		this.argument = argument;
	}
	
	public CommandLineArg(int position, String name, boolean isFlag, boolean isRequired, String argument) {
		this(new CommandLineArgDef(position, name, isFlag, isRequired), argument);
	}
	
	public final String getArgument() {
		return this.argument;
	}
	
	public final String getArgumentName() {
		return this.definition.getArgName();
	}
	
	public final int getArgumentPosition() {
		return this.definition.getArgPosition();
	}
	
	public final CommandLineArgDef getCommandLineArgDef() {
		return this.definition;
	}
	
	public final boolean isFlag() {
		return this.definition.isFlag();
	}
	
}
