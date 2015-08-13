
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
package uk.ac.stfc.isis.ibex.log.message.sql;

/**
 * Names for log message fields as used for WHERE clauses in the SQL database
 */
final class LogMessageWhereSqlFieldTags {
	static final String TAG_CONTENTS = "message.contents";
	static final String TAG_SEVERITY = "message_severity.severity";
	static final String TAG_EVENTTIME = "message.eventTime";
	static final String TAG_CREATETIME = "message.createTime";
	static final String TAG_CLIENTNAME = "client_name.name";
	static final String TAG_CLIENTHOST = "client_host.name";
	static final String TAG_TYPE = "message_type.type";
	static final String TAG_APPID = "application.name";
	
	private LogMessageWhereSqlFieldTags() { }
}

public enum LogMessageFieldsWhereSql {
	CONTENTS ("Content", LogMessageWhereSqlFieldTags.TAG_CONTENTS, 500),
	SEVERITY ("Severity", LogMessageWhereSqlFieldTags.TAG_SEVERITY, 100),
	EVENT_TIME ("Event Time", LogMessageWhereSqlFieldTags.TAG_EVENTTIME, 200),
	CREATE_TIME ("Create Time", LogMessageWhereSqlFieldTags.TAG_CREATETIME, 0),
	CLIENT_NAME ("Sender", LogMessageWhereSqlFieldTags.TAG_CLIENTNAME, 150),
	CLIENT_HOST ("Sender Host", LogMessageWhereSqlFieldTags.TAG_CLIENTHOST, 0),
	TYPE ("Type", LogMessageWhereSqlFieldTags.TAG_TYPE, 100),
	APPLICATION_ID ("Application ID", LogMessageWhereSqlFieldTags.TAG_APPID, 0);
	
	private String displayName;
	private String tagName;
	private LogMessageFieldsWhereSql(String displayName, String tagName, int defaultColumnWidth) {
		this.displayName = displayName;
		this.tagName = tagName;
	}

	public String getTagName() {
		return tagName;
	}

	@Override
	public String toString() {
		return displayName;
	}
	
}
