
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

package uk.ac.stfc.isis.ibex.ui.log.widgets;

import java.util.Calendar;

import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;

/**
 * Interface implemented by classes searching IOC log messages.
 */
public interface ISearchModel {
    
    /**
     * Performs a search, returning all log messages that match the request
     * parameters.
     * 
     * @param field The log message field to search by.
     * @param value Search the 'searchField' field of every record for this
     *            string value
     * @param from Consider only messages that occurred after this time (null =
     *            no limit).
     * @param to Consider only messages that occurred before this time (null =
     *            no limit).
     */
	void search(LogMessageFields field, String value, Calendar from, Calendar to);

	/**
     * Clear search results and return to the 'live message' view (i.e. display
     * messages in the cache).
     */
	void clearSearch();
}
