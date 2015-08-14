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

import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;

public class LogMessageSql {
    /**
     * @param searchField
     * @return the appropriate SQL for where lookups
     */
    public LogMessageFieldsWhereSql getWhereSqlTag(LogMessageFields searchField) {
	switch (searchField) {
	case CONTENTS:
	    return LogMessageFieldsWhereSql.CONTENTS;
	case SEVERITY:
	    return LogMessageFieldsWhereSql.SEVERITY;
	case EVENT_TIME:
	    return LogMessageFieldsWhereSql.EVENT_TIME;
	case CREATE_TIME:
	    return LogMessageFieldsWhereSql.CREATE_TIME;
	case CLIENT_NAME:
	    return LogMessageFieldsWhereSql.CLIENT_NAME;
	case CLIENT_HOST:
	    return LogMessageFieldsWhereSql.CLIENT_HOST;
	case TYPE:
	    return LogMessageFieldsWhereSql.TYPE;
	case APPLICATION_ID:
	    return LogMessageFieldsWhereSql.APPLICATION_ID;
	default:
	    new Throwable().getStackTrace();
	    return null;
	}
    }
}
