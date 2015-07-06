
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

package uk.ac.stfc.isis.ibex.log;

import java.util.HashMap;
import java.util.Map;

import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;

/*
 * Provides a count of the number of messages received 
 * by severity or for all severities.
 */
public class MessageCounter {
	
	private Map<String, Long> counts = new HashMap<String, Long>(); 
	private long totalCount = 0;
	
	public long totalCount() {
		return totalCount;
	}
	
	public long countsForSeverity(String severity) {		
		return counts.containsKey(severity) ? counts.get(severity) : 0;
	}
	
	public void countMessage(LogMessage message) {
		incrementCount(severity(message));
	}
			
	private void incrementCount(String messageSeverity) {
		incrementSeverity(messageSeverity);
		totalCount++;
	}

	private void incrementSeverity(String messageSeverity) {
		counts.put(messageSeverity, countsForSeverity(messageSeverity) + 1);
	}
	
	private String severity(LogMessage logMessage) {
		return logMessage.getProperty(LogMessageFields.SEVERITY);
	}
}
