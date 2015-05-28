/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.ui.log.filter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;

public class LogMessageFilter extends ViewerFilter {
	private String filterString;
	private LogMessageFields filterfield;
	private boolean isNegative;
	
	public LogMessageFilter(LogMessageFields field, String filterValue, boolean isNegative) {
		this.filterfield = field;
		this.filterString = filterValue;
		this.isNegative = isNegative;
	}
	
	public void setFilter(String value) {
		this.filterString = ".*" + value + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return showMessage((LogMessage) element);
	}

	public boolean showMessage(LogMessage message) {
		if (filterString == null) {
			return true;
		}

		if (message.getProperty(filterfield).matches(
						filterString)) {
			return !isNegative;
		}

		return isNegative;
	}

	public List<LogMessage> getFilteredList(final List<LogMessage> unfiltered) {
		List<LogMessage> filtered = new ArrayList<LogMessage>();

		for (LogMessage msg : unfiltered) {
			if (showMessage(msg)) {
				filtered.add(msg);
			}
		}

		return filtered;
	}
}
