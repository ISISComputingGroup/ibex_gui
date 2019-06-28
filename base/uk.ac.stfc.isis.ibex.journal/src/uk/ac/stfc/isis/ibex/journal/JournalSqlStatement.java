
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2019
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.journal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Prepares the SQL statement which searches the journal database.
 * 
 * NOTE: Ensure that arbitrary strings cannot be inserted into this query, maliciously or 
 * accidentally as that could lead to a SQL injection vulnerability.
 * 
 */
public class JournalSqlStatement {
    private int pageNumber;
    private int pageSize;
    private Connection connection;
    private JournalField field;
    private ArrayList<JournalField> sortFields = new ArrayList<JournalField>();
    private ArrayList<String> sortDirections = new ArrayList<String>();
    private static final String SELECT = "SELECT * FROM journal_entries";
    
    /**
     * Create a journal SQL statement.
     * 
     * @param pageNumber The page number to get the data for.
     * @param pageSize The number of rows on each page.
     * @param connection the SQL connection to use.
     * @param field The journal field to search by.
     */
    public JournalSqlStatement(int pageNumber, int pageSize, Connection connection, JournalField field) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.connection = connection;
        this.field = field;
    }
    
    /**
     * Constructs a prepared statement which gets journal data from the database with no filtering.
     * 
     * @return An SQL PreparedStatement
     * @throws SQLException - If there was an error while querying the database
     */
    public PreparedStatement constructDefaultQuery() throws SQLException{
        StringBuilder query = new StringBuilder(SELECT);
        query.append(getSortLimitStatement());
        PreparedStatement st = connection.prepareStatement(query.toString());
        st.setInt(1, (pageNumber - 1) * pageSize);
        st.setInt(2, pageSize);
        return st;
    }
    
    /**
     * Constructs a prepared statement which searches the database with a text search.
     * 
     * @param searchString Search the 'searchField' field of every record for this string value
     * @return An SQL PreparedStatement
     * @throws SQLException - If there was an error while querying the database
     */
    public PreparedStatement constructTextSearchQuery(String searchString) throws SQLException {
        StringBuilder query = new StringBuilder(SELECT);
        query.append(" WHERE " + field.getSqlFieldName() + " LIKE ?");
        query.append(getSortLimitStatement());
        PreparedStatement st = connection.prepareStatement(query.toString());
        st.setString(1, "%" + searchString + "%");
        st.setInt(2, (pageNumber - 1) * pageSize);
        st.setInt(3, pageSize);
        return st;
    }
    
    /**
     * Constructs a prepared statement which searches the database with number parameters.
     * 
     * @param fromNumber Consider only runs with a run number from this number and up (null = no limit)
     * @param toNumber Consider only runs with a run number from this number and below (null = no limit)
     * @return An SQL PreparedStatement
     * @throws SQLException - If there was an error while querying the database
     */
    public PreparedStatement constructNumberSearchQuery(Integer fromNumber, Integer toNumber) throws SQLException {
        StringBuilder query = new StringBuilder(SELECT);
        query.append(" WHERE");
        if (fromNumber != null) {
            query.append(" " + field.getSqlFieldName() + " >= ?");
        }
        if (fromNumber != null && toNumber != null) {
            query.append(" AND");
        }
        if (toNumber != null) {
            query.append(" " + field.getSqlFieldName() + " <= ?");
        }
        query.append(getSortLimitStatement());
        System.out.println(query);
        PreparedStatement st = connection.prepareStatement(query.toString());
        
        int index = 0;
        if (fromNumber != null) {
            st.setInt(++index, fromNumber);
        }
        if (toNumber != null) {
            st.setInt(++index, toNumber);
        }
        st.setInt(++index, (pageNumber - 1) * pageSize);
        st.setInt(++index, pageSize);
        return st;
    }
    
    /**
     * Constructs a prepared statement which searches the database with time parameters.
     * 
     * @param fromTime Consider only runs with a start time after this time (null = no limit)
     * @param toTime Consider only runs with a start time before this time (null = no limit)
     * @return An SQL PreparedStatement
     * @throws SQLException - If there was an error while querying the database
     */
    public PreparedStatement constructTimeSearchQuery(Calendar fromTime, Calendar toTime) throws SQLException {
        StringBuilder query = new StringBuilder(SELECT);
        query.append(" WHERE");
        if (fromTime != null) {
            query.append(" " + field.getSqlFieldName() + " >= ?");
        }
        if (fromTime != null && toTime != null) {
            query.append(" AND");
        }
        if (toTime != null) {
            query.append(" " + field.getSqlFieldName() + " <= ?");
        }
        query.append(getSortLimitStatement());
        PreparedStatement st = connection.prepareStatement(query.toString());
        
        int index = 0;
        if (fromTime != null) {
            Timestamp timestamp = new Timestamp(fromTime.getTimeInMillis());
            st.setTimestamp(++index, timestamp);
        }
        if (toTime != null) {
            Timestamp timestamp = new Timestamp(toTime.getTimeInMillis());
            st.setTimestamp(++index, timestamp);
        }
        st.setInt(++index, (pageNumber - 1) * pageSize);
        st.setInt(++index, pageSize);
        return st;
    }
    
    /**
     * Adds a field to be used to order the results in ascending order.
     * 
     * @param sortField
     */
    public void addAscendingSort(JournalField sortField) {
        sortFields.add(sortField);
        sortDirections.add(" ASC");
    }
    
    /**
     * Adds a field to be used to order the results in descending order.
     * 
     * @param sortField
     */
    public void addDescendingSort(JournalField sortField) {
        sortFields.add(sortField);
        sortDirections.add(" DESC");
    }
    
    /**
     * @return A string containing the ORDER BY and LIMIT sections of the query.
     */
    private String getSortLimitStatement() {
        StringBuilder statement = new StringBuilder();
        if (sortFields.size() > 0) {
            statement.append(" ORDER BY " + sortFields.get(0).getSqlFieldName() + sortDirections.get(0));
            for (int i = 1; i < sortFields.size(); i++) {
                statement.append(", " + sortFields.get(i).getSqlFieldName() + sortDirections.get(i));
            }
        }
        statement.append(" LIMIT ?, ?");
        return statement.toString();
    }
    
}
