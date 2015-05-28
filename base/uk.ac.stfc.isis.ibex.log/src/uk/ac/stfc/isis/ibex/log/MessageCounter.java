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
