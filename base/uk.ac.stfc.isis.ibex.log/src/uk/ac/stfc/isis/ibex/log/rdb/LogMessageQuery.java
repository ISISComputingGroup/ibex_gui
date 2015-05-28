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

public class LogMessageQuery {
	private static final LogMessageFields[] SQL_SELECT_FIELDS = {
		LogMessageFields.CREATE_TIME,
		LogMessageFields.EVENT_TIME,
		LogMessageFields.TYPE,
		LogMessageFields.CONTENTS,
		LogMessageFields.CLIENT_NAME,
		LogMessageFields.SEVERITY,
		LogMessageFields.CLIENT_HOST,
		LogMessageFields.APPLICATION_ID
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
	public List<LogMessage> getMessages(LogMessageFields searchField, String searchValue, 
			Calendar from, Calendar to) throws Exception {
		LogMessageFields[] whereLikeClauses = { searchField };
		
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
			LogMessageFields[] fields) throws SQLException {
		LogMessage newMessage = new LogMessage();
		
		int ind = 0;
		for (LogMessageFields field: fields) {
			String value = result.getString(++ind);
			newMessage.setProperty(field, value);
		}
		
		return newMessage;
	}
}
