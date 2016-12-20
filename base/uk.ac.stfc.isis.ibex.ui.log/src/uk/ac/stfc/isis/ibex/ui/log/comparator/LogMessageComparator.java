
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

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

import uk.ac.stfc.isis.ibex.activemq.message.LogMessage;
import uk.ac.stfc.isis.ibex.activemq.message.LogMessageFields;

public class LogMessageComparator extends ViewerComparator implements Comparator<LogMessage> {
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
