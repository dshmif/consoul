package com.abstractthis.consoul.filter;

import java.util.ArrayList;
import java.util.List;

public final class FilterExecutor {
	private static final FilterExecutor EXECUTOR = new FilterExecutor();
	private static final List<ConsoleFilter> IN_FILTERS = new ArrayList<ConsoleFilter>();
	private static final List<ConsoleFilter> OUT_FILTERS = new ArrayList<ConsoleFilter>();
	
	private FilterExecutor() {}
	
	public static FilterExecutor getExecutor() {
		return EXECUTOR;
	}
	
	public final void executeIn() {
		for(ConsoleFilter filter : IN_FILTERS) {
			filter.apply();
		}
	}
	
	public final void executeOut() {
		for(ConsoleFilter filter : OUT_FILTERS) {
			filter.apply();
		}
	}
	
}
