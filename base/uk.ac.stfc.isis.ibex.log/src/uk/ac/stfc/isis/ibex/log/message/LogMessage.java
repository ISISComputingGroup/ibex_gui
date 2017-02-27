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

package uk.ac.stfc.isis.ibex.log.message;

import uk.ac.stfc.isis.ibex.activemq.message.IMessage;
import uk.ac.stfc.isis.ibex.log.message.sql.LogMessageFieldsSql;

/**
 * This class is responsible for maintaining a log message either in relation to
 * the XML or SQL database.
 */
public class LogMessage implements IMessage {
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
	switch (property) {
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
	switch (property) {
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

    public void setProperty(LogMessageFieldsSql property, String value) {
	switch (property) {
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
}
