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
package uk.ac.stfc.isis.ibex.log.message;

public class LogMessage {
	// message info
	private String contents = "";
	private String severity = "";
	private String eventTime = "";
	private String clientName = "";
	private String type = "";
	
	// meta info
	private String clientHost = "";
	private String createTime = "";
	private String applicationId = "";
	
	public void setProperty(LogMessageFields property, String value) {
		switch(property) {
			case CONTENTS:
				contents = value;
				break;
			case SEVERITY:
				severity = value;
				break;
			case EVENT_TIME:
				eventTime = value;
				break;
			case CREATE_TIME:
				createTime = value;
				break;
			case CLIENT_NAME:
				clientName = value;
				break;
			case CLIENT_HOST:
				clientHost = value;
				break;
			case TYPE:
				type = value;
				break;
			case APPLICATION_ID:
				applicationId = value;
				break;
			default:
				new Throwable().getStackTrace();
		}
	}
	
	public String getProperty(LogMessageFields property) {
		switch(property) {
			case CONTENTS:
				return contents;
			case SEVERITY:
				return severity;
			case EVENT_TIME:
				return eventTime;
			case CREATE_TIME:
				return createTime;	
			case CLIENT_NAME:
				return clientName;
			case CLIENT_HOST:
				return clientHost;
			case TYPE:
				return type;
			case APPLICATION_ID:
				return applicationId;
			default:
				new Throwable().getStackTrace();
				return null;
		}
	}
	
	public Object[] getProperties(LogMessageFields[] properties) {
		int length = properties.length;
		
		String[] values = new String[length];
		
		for (int p = 0; p < length; ++p) {
			values[p] = getProperty(properties[p]);
		}
		
		return values;		
	}
}
