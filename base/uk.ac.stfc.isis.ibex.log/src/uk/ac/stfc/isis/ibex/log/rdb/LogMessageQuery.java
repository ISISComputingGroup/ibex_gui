
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;
import uk.ac.stfc.isis.ibex.log.message.sql.LogMessageFieldsSql;
import uk.ac.stfc.isis.ibex.log.message.sql.LogMessageFieldsWhereSql;
import uk.ac.stfc.isis.ibex.log.message.sql.LogMessageSql;

public class LogMessageQuery {
	private static final LogMessageFieldsSql[] SQL_SELECT_FIELDS = {
		LogMessageFieldsSql.CREATE_TIME,
		LogMessageFieldsSql.EVENT_TIME,
		LogMessageFieldsSql.TYPE,
		LogMessageFieldsSql.CONTENTS,
		LogMessageFieldsSql.CLIENT_NAME,
		LogMessageFieldsSql.SEVERITY,
		LogMessageFieldsSql.CLIENT_HOST,
		LogMessageFieldsSql.APPLICATION_ID
	};
	
	private final Rdb rdb;
	
	private final SqlStatement sqlStatement;

	public LogMessageQuery(Rdb rdb) throws Exception {
		this.rdb = rdb;
		this.sqlStatement = new SqlStatement();
	}
		
	/**
	 * Performs a database search, returning all log messages in the DB that match the request parameters.
	 * @param searchField The log message field to search by.
	 * @param searchValue Search the 'searchField' field of every record for this string value
	 * @param from Consider only messages that occurred after this time (null = no limit).
	 * @param to Consider only messages that occurred before this time (null = no limit).
	 */
	public List<LogMessage> getMessages(LogMessageFieldsWhereSql searchField, String searchValue, 
			Calendar from, Calendar to) throws Exception {
		LogMessageFieldsWhereSql[] whereLikeClauses = { searchField };
		
		sqlStatement.setSelectFields(SQL_SELECT_FIELDS);
		sqlStatement.setWhereLikeClause(whereLikeClauses);
		sqlStatement.setTimeRange(from, to);
		
		String selectStatement = sqlStatement.getSelectStatement();
		
		final PreparedStatement statement = rdb.getConnection().prepareStatement(selectStatement);
				
		ArrayList<LogMessage> messages = new ArrayList<LogMessage>();
		
		String messageSearch = "%" +  searchValue + "%";
		
		try {
			int index = 0;
			statement.setString(++index, messageSearch);
			
			if (from != null) {
				Timestamp start = new Timestamp(from.getTimeInMillis());
				statement.setTimestamp(++index, start);
			}
			
			if (to != null) {
				Timestamp end = new Timestamp(to.getTimeInMillis());
				statement.setTimestamp(++index, end);
			}
			
			ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				LogMessage message = getMessageFromQueryResult(result, SQL_SELECT_FIELDS);
				messages.add(message);
			}
		} finally {
			statement.close();
		}

		return messages;
	}
	
	/**
	 * For a single query result, populate the fields of a new log message. Fields must be
	 * in the same order as they were in the select statement.
	 */
	private LogMessage getMessageFromQueryResult(ResultSet result, 
			LogMessageFieldsSql[] fields) throws SQLException {
		LogMessage newMessage = new LogMessage();
		
		int ind = 0;
		for (LogMessageFieldsSql field: fields) {
			String value = result.getString(++ind);
			newMessage.setProperty(field, value);
		}
		
		return newMessage;
	}
	
	/**
	 * Performs a database search, returning all log messages in the DB that match the request parameters.
	 * Converts the LogMessageField to the appropriate LogMessageFieldSql to allow for a schema change
	 * @param searchField The log message field to search by.
	 * @param searchValue Search the 'searchField' field of every record for this string value
	 * @param from Consider only messages that occurred after this time (null = no limit).
	 * @param to Consider only messages that occurred before this time (null = no limit).
	 */
	public List<LogMessage> getMessages(LogMessageFields searchField, String searchValue, 
			Calendar from, Calendar to) throws Exception {
		LogMessageFieldsWhereSql field = new LogMessageSql().getWhereSqlTag(searchField);
		return getMessages(field, searchValue, from, to);
	}
}
