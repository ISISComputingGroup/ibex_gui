
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
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.journal.JournalSort.JournalSortDirection;

/**
 * Prepares the SQL statement which searches the journal database.
 * 
 * NOTE: Ensure that arbitrary strings cannot be inserted into this query, maliciously or 
 * accidentally as that could lead to a SQL injection vulnerability.
 * 
 */
public class JournalSqlStatement {
    private JournalParameters parameters;
    private int pageNumber;
    private int pageSize;
    private Connection connection;
    private ArrayList<JournalSort> sorts = new ArrayList<JournalSort>();
    private static final String SELECT = "SELECT * FROM journal_entries";
    
    /**
     * Create a journal SQL statement.
     * 
     * @param parameters The search parameters.
     * @param pageNumber The page number to get the data for.
     * @param pageSize The number of rows on each page.
     * @param connection the SQL connection to use.
     */
    public JournalSqlStatement(JournalParameters parameters, int pageNumber, int pageSize, Connection connection) {
        this.parameters = parameters;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.connection = connection;
    }
    
    /**
     * Constructs a prepared statement for getting journal data from the database.
     * 
     * @return An SQL PreparedStatement
     * @throws SQLException - If there was an error while querying the database
     */
    public PreparedStatement constructQuery() throws SQLException {
        StringBuilder query = new StringBuilder(SELECT);
        
        query.append(createWhereTemplate());
        query.append(createSortLimitTemplate());
        
        return fillTemplate(query.toString());
    }
    
    /**
     * @return A string containing the WHERE section of the query.
     */
    private String createWhereTemplate() {
        StringBuilder query = new StringBuilder();
        if (parameters.hasOptionalParameters()) {
            query.append(" WHERE ");
            ArrayList<String> parts = new ArrayList<String>();
            parameters.searchString.ifPresent(x -> parts.add(parameters.field.getSqlFieldName() + " LIKE ?"));
            parameters.fromNumber.ifPresent(x -> parts.add(parameters.field.getSqlFieldName() + " >= ?"));
            parameters.toNumber.ifPresent(x -> parts.add(parameters.field.getSqlFieldName() + " <= ?"));
            parameters.fromTime.ifPresent(x -> parts.add(parameters.field.getSqlFieldName() + " >= ?"));
            parameters.toTime.ifPresent(x -> parts.add(parameters.field.getSqlFieldName() + " <= ?"));
            query.append(String.join(" AND ", parts));
        }
        return query.toString();
    }
    
    /**
     * @param query The string query template to fill.
     * @return A full prepared statement ready to be executed.
     * @throws SQLException - If there was an error while querying the database
     */
    private PreparedStatement fillTemplate(String query) throws SQLException {
        PreparedStatement st = connection.prepareStatement(query.toString());
        int index = 0;
        
        if (parameters.searchString.isPresent()) {
            st.setString(++index, "%" + parameters.searchString.get() + "%");
        }
        if (parameters.fromNumber.isPresent()) {
            st.setInt(++index, parameters.fromNumber.get());
        }
        if (parameters.toNumber.isPresent()) {
            st.setInt(++index, parameters.toNumber.get());
        }
        if (parameters.fromTime.isPresent()) {
            st.setTimestamp(++index, new Timestamp(parameters.fromTime.get().getTimeInMillis()));
        }
        if (parameters.toTime.isPresent()) {
            st.setTimestamp(++index, new Timestamp(parameters.toTime.get().getTimeInMillis()));
        }
        
        st.setInt(++index, (pageNumber - 1) * pageSize);
        st.setInt(++index, pageSize);
        return st;
    }
    
    /**
     * @return A string containing the ORDER BY and LIMIT sections of the query.
     */
    private String createSortLimitTemplate() {
        if (sorts.size() == 0) {
            throw new IllegalStateException("Cannot not sort data.");
        }
        StringBuilder statement = new StringBuilder();
        statement.append(" ORDER BY ");

        statement.append(sorts.stream()
                .map(js -> js.sortField.getSqlFieldName() + " " + js.direction.getSql())
                .collect(Collectors.joining(", ")));
        
        statement.append(" LIMIT ?, ?");
        return statement.toString();
    }
    
    /**
     * Adds a field to be used to order the results in ascending order.
     * 
     * @param sortField The field to sort with.
     */
    public void addAscendingSort(JournalField sortField) {
        sorts.add(new JournalSort(sortField, JournalSortDirection.ASCENDING));
    }
    
    /**
     * Adds a field to be used to order the results in descending order.
     * 
     * @param sortField The field to sort with.
     */
    public void addDescendingSort(JournalField sortField) {
        sorts.add(new JournalSort(sortField, JournalSortDirection.DESCENDING));
    }
    
}
