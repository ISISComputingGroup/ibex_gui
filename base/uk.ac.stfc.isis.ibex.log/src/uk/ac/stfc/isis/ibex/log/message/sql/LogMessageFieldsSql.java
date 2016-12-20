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

package uk.ac.stfc.isis.ibex.log.message.sql;

/**
 * Names for log message fields as used in the SQL database.
 */
final class LogMessageSqlFieldTags {
    static final String TAG_CONTENTS = "contents";
    static final String TAG_SEVERITY = "severity_id";
    static final String TAG_EVENTTIME = "eventTime";
    static final String TAG_CREATETIME = "createTime";
    static final String TAG_CLIENTNAME = "clientName_id";
    static final String TAG_CLIENTHOST = "clientHost_id";
    static final String TAG_TYPE = "type_id";
    static final String TAG_APPID = "application_id";

    private LogMessageSqlFieldTags() {
    }
}

public enum LogMessageFieldsSql {
    CONTENTS("Content", LogMessageSqlFieldTags.TAG_CONTENTS, 500), SEVERITY(
	    "Severity", LogMessageSqlFieldTags.TAG_SEVERITY, 100), EVENT_TIME(
	    "Event Time", LogMessageSqlFieldTags.TAG_EVENTTIME, 200), CREATE_TIME(
	    "Create Time", LogMessageSqlFieldTags.TAG_CREATETIME, 0), CLIENT_NAME(
	    "Sender", LogMessageSqlFieldTags.TAG_CLIENTNAME, 150), CLIENT_HOST(
	    "Sender Host", LogMessageSqlFieldTags.TAG_CLIENTHOST, 0), TYPE(
	    "Type", LogMessageSqlFieldTags.TAG_TYPE, 100), APPLICATION_ID(
	    "Application ID", LogMessageSqlFieldTags.TAG_APPID, 0);

    private String displayName;

    LogMessageFieldsSql(String displayName, String tagName,
	    int defaultColumnWidth) {
	this.displayName = displayName;
    }

    @Override
    public String toString() {
	return displayName;
    }
}
