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
package uk.ac.stfc.isis.ibex.log.rdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.log.Log;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;
import uk.ac.stfc.isis.ibex.log.preferences.PreferenceConstants;

/**
 * Prepares the SQL 'insert into' statement for adding a message to the database.
 *
 */
public class SqlStatement {
    private static final String TABLENAME = "message";
    
    private String schemaName;
    private static int LIMIT = 1000;
    
    private List<LogMessageFields> selectFields;
    private List<LogMessageFields> whereLikeFields;
    
    private Calendar startTime;
    private Calendar endTime;
    
    public SqlStatement() {
    	IPreferenceStore preferenceStore = Log.getDefault().getPreferenceStore();
    	schemaName = preferenceStore.getString(PreferenceConstants.P_SQL_SCHEMA);
    	
    	this.selectFields = new ArrayList<LogMessageFields>();
    	this.whereLikeFields = new ArrayList<LogMessageFields>();
    }
    
    /**
     * Set the (ordered) list of fields that will be retrieved by the select statement
     */
    public void setSelectFields(LogMessageFields[] selectFields) {
    	this.selectFields = new ArrayList<LogMessageFields>(Arrays.asList(selectFields));
    }
    
    /**
     * Set the ordered list of fields that will be featured in "WHERE field LIKE ?" clauses.
     */
    public void setWhereLikeClause(LogMessageFields[] whereLikeFields) {
    	this.whereLikeFields = new ArrayList<LogMessageFields>(Arrays.asList(whereLikeFields));
    }
    
    /**
     * Set the limits in which to search in terms of event time. Null values ignored.
     */
    public void setTimeRange(Calendar startTime, Calendar endTime) {
    	this.startTime = startTime;
    	this.endTime = endTime;
    }
    
    /**
     * Return a string representation of the SQL select statement with SELECT fields, 
     * WHERE clauses and time restrictions as appropriate. The values of the parameters
     * to be filled in are given standard placeholders (?). 
     */
    public String getSelectStatement() {   	
		return "SELECT " + selectList() 
	            + " FROM " + schemaName + "." + TABLENAME 
	            + " WHERE " + whereList()
	            + " LIMIT " + Integer.toString(LIMIT);
    }

    /**
     * Get string representation of the SELECT list
     */
	private String selectList() {
		StringBuilder cols = new StringBuilder();
    	
		if (selectFields != null && selectFields.size() > 0) {
			cols.append(selectFields.get(0).getTagName());
			
	    	for (int i = 1; i < selectFields.size(); ++i) {
	    		cols.append(", " + selectFields.get(i).getTagName());
	    	}
		}
    	
    	return cols.toString();
	}
	
	/**
	 * Get string representation of the list of WHERE clauses, including
	 * the date-time clauses.
	 */
	private String whereList() {
		StringBuilder where = new StringBuilder();
		
		// 'WHERE field Like ?'
		if (whereLikeFields.size() > 0) {
			String clause = whereLikeFields.get(0).getTagName() + " LIKE ?";
			where.append(clause);
			
	    	for (int i = 1; i < whereLikeFields.size(); ++i) {
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
