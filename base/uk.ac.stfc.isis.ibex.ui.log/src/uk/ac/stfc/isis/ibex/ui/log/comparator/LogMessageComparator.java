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
package uk.ac.stfc.isis.ibex.ui.log.comparator;

import java.util.Comparator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;

public class LogMessageComparator extends ViewerComparator implements Comparator<LogMessage>
{
	private LogMessageFields messageField;
	
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;
	
	public LogMessageComparator() {
		this.messageField = LogMessageFields.EVENT_TIME;
	  	direction = DESCENDING;
	}

	public int getDirection() {
	    return direction == 1 ? SWT.DOWN : SWT.UP;
	}

	public void setColumn(LogMessageFields column) {
	    if (column == this.messageField) {
	    	// Same column as last sort; toggle the direction
	    	direction = 1 - direction;
	    } else {
	    	// New column; do an ascending sort
	    	this.messageField = column;
	    	direction = DESCENDING;
	    }
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
	    return compare((LogMessage) e1, (LogMessage) e2);
	}
	
	@Override
	public int compare(LogMessage lm1, LogMessage lm2) {
		int rc = 0;
		
		rc = lm1.getProperty(messageField).compareTo(lm2.getProperty(messageField));
	    
		// If descending order, flip the direction
		if (direction == DESCENDING) {
			return -rc;
		}
	    
		return rc;
	}


}
