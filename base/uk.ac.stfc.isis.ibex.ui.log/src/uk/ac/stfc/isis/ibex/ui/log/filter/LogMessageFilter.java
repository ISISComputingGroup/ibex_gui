
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
package uk.ac.stfc.isis.ibex.ui.log.filter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;

/**
 * The class that filters the log messages in the table based on the user's
 * inputed criteria.
 */
public class LogMessageFilter extends ViewerFilter {
	private String filterString;
	private LogMessageFields filterfield;
	private boolean isNegative;
	
    /**
     * Creates the filter based on the provided options.
     * 
     * If isNegative is False the filter will return all messages where the
     * specified field contains the specified value. If isNegative is True the
     * filter will return all messages where the specified field does not
     * contain the specified value.
     * 
     * @param filterField
     *            The message field to filter on.
     * @param filterValue
     *            The value to filter on.
     * @param isNegative
     *            Whether to negatively filter (see above)
     */
    public LogMessageFilter(LogMessageFields filterField, String filterValue, boolean isNegative) {
        this.filterfield = filterField;
		this.filterString = filterValue;
		this.isNegative = isNegative;
	}
	
    /**
     * Set the value to filter on.
     * 
     * @param value
     *            The value to filter on.
     */
	public void setFilter(String value) {
		this.filterString = ".*" + value + ".*";
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return showMessage((LogMessage) element);
	}

    /**
     * Checks a singular message for whether or not it should be displayed.
     * 
     * @param message
     *            The message to check.
     * @return True if the message should be displayed to the user.
     */
    private boolean showMessage(LogMessage message) {
		if (filterString == null) {
			return true;
		}

		if (message.getProperty(filterfield).matches(
						filterString)) {
			return !isNegative;
		}

		return isNegative;
	}

    /**
     * Gets all the messages that adhere to the given filtering criteria.
     * 
     * @param unfiltered
     *            The list of unfiltered messages.
     * @return The list of filtered messages.
     */
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
