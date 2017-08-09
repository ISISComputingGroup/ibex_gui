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

package uk.ac.stfc.isis.ibex.log.rdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.log.Log;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;
import uk.ac.stfc.isis.ibex.log.message.sql.LogMessageFieldsSql;
import uk.ac.stfc.isis.ibex.log.message.sql.LogMessageFieldsWhereSql;
import uk.ac.stfc.isis.ibex.log.preferences.PreferenceConstants;

/**
 * Prepares the SQL 'insert into' statement for adding a message to the
 * database.
 * 
 */
public class SqlStatement {
    private static final String TABLENAME = "message";

    private String schemaName;
    private static final int LIMIT = 1000;

    @SuppressWarnings("unused")
    private List<LogMessageFieldsSql> selectFields;
    private List<LogMessageFieldsWhereSql> whereLikeFields;

    private Calendar startTime;
    private Calendar endTime;

    public SqlStatement() {
	IPreferenceStore preferenceStore = Log.getDefault()
		.getPreferenceStore();
	schemaName = preferenceStore
		.getString(PreferenceConstants.P_MESSAGE_SQL_SCHEMA);

	this.selectFields = new ArrayList<LogMessageFieldsSql>();
	this.whereLikeFields = new ArrayList<LogMessageFieldsWhereSql>();
    }

    /**
     * Set the (ordered) list of fields that will be retrieved by the select
     * statement.
     * 
     * @param selectFields
     *            the fields to select
     */
    public void setSelectFields(LogMessageFieldsSql[] selectFields) {
	this.selectFields = new ArrayList<LogMessageFieldsSql>(
		Arrays.asList(selectFields));
    }

    /**
     * Set the ordered list of fields that will be featured in "WHERE field LIKE
     * ?" clauses.
     * 
     * @param whereLikeFields
     *            the where like fields to select
     */
    public void setWhereLikeClause(LogMessageFieldsWhereSql[] whereLikeFields) {
	this.whereLikeFields = new ArrayList<LogMessageFieldsWhereSql>(
		Arrays.asList(whereLikeFields));
    }

    /**
     * Set the limits in which to search in terms of event time. Null values
     * ignored.
     * 
     * @param startTime
     *            The earliest time to include in the search
     * @param endTime
     *            The latest time to include in the search
     */
    public void setTimeRange(Calendar startTime, Calendar endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Return a string representation of the SQL select statement with SELECT
     * fields, WHERE clauses and time restrictions as appropriate. The values of
     * the parameters to be filled in are given standard placeholders (?).
     * 
     * @return A SQL statement
     */
    public String getSelectStatement() {
	return "SELECT " + selectList() + " FROM " + selectTableList()
		+ " WHERE " + whereList() + " LIMIT " + Integer.toString(LIMIT);
    }

    /**
     * Get string representation of the SELECT list
     */
    private String selectList() {
	StringBuilder cols = new StringBuilder();

	// Name the columns in order as this is now a composite lookup
	cols.append("message.createTime, message.eventTime, message_type.type, message.contents, ");
	cols.append("client_name.name, message_severity.severity, client_host.name, application.name, message.repeatCount");

	return cols.toString();
    }

    /**
     * Get string representation of the Tables list
     */
    private String selectTableList() {
	StringBuilder tables = new StringBuilder();
	// Append the main table
	tables.append(schemaName + "." + TABLENAME);
	// Append the other tables in use
	tables.append(", " + schemaName + ".message_severity");
	tables.append(", " + schemaName + ".client_name");
	tables.append(", " + schemaName + ".client_host");
	tables.append(", " + schemaName + ".message_type");
	tables.append(", " + schemaName + ".application");
	return tables.toString();
    }

    /**
     * Get string representation of the list of WHERE clauses, including the
     * date-time clauses.
     */
    private String whereList() {
	StringBuilder where = new StringBuilder();

	// Link the different tables together
	where.append("message_severity.id = message.severity_id");
	where.append(" AND client_name.id = message.clientName_id");
	where.append(" AND client_host.id = message.clientHost_id");
	where.append(" AND message_type.id = message.type_id");
	where.append(" AND application.id = message.application_id");

	// 'WHERE field Like ?'
	if (whereLikeFields.size() > 0) {
	    String clause = null;
	    for (int i = 0; i < whereLikeFields.size(); ++i) {
		clause = whereLikeFields.get(i).getTagName() + " LIKE ?";
		where.append(" AND " + clause);
	    }
	}

	if (startTime != null) {
	    String clause = LogMessageFields.EVENT_TIME.getTagName() + " > ?";
	    where.append(" AND " + clause);
	}

	if (endTime != null) {
	    String clause = LogMessageFields.EVENT_TIME.getTagName() + " < ?";
	    where.append(" AND " + clause);
	}

	return where.toString();
    }
}
