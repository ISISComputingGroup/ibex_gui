
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
 */
public class JournalSqlStatement {
    private int pageNumber;
    private int pageSize;
    
    /**
     * Create a journal SQL statement
     * 
     * @param pageNumber The page number to get the data for
     * @param pageSize The number of rows on each page
     */
    public JournalSqlStatement(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }
    
    /**
     * Constructs a PreparedStatement using the searching and sorting parameters
     * 
     * @param connection the SQL connection to use.
     * @param field The journal field to search by.
     * @param searchString Search the 'searchField' field of every record for this string value (null = no string search)
     * @param fromNumber Consider only runs with a run number from this number and up (null = no limit)
     * @param toNumber Consider only runs with a run number from this number and below (null = no limit)
     * @param fromTime Consider only runs with a start time after this time (null = no limit)
     * @param toTime Consider only runs with a start time before this time (null = no limit)
     * @param sortFields A list of fields to be used to order the results
     * @param sortOrders A list of orders for each of the sort fields, either ASC or DESC
     * @throws SQLException - If there was an error while querying the database
     * @return An SQL PreparedStatement
     */
    public PreparedStatement constructSearchQuery(Connection connection, JournalField field, String searchString,
            Integer fromNumber, Integer toNumber, Calendar fromTime, Calendar toTime,
            ArrayList<JournalField> sortFields, ArrayList<String> sortOrders) throws SQLException {
        
        StringBuilder query = new StringBuilder("SELECT * FROM journal_entries");

        if (searchString != null || fromNumber != null || toNumber != null || fromTime != null || toTime != null) {
            query.append(" WHERE");
            boolean firstCondition = true;
            if (searchString != null) {
                query.append(firstCondition ? " " : " AND ");
                firstCondition = false;
                query.append(field.getSqlFieldName() + " LIKE ?");
            }
            if (fromNumber != null) {
                query.append(firstCondition ? " " : " AND ");
                firstCondition = false;
                query.append(field.getSqlFieldName() + " >= ?");
            }
            if (toNumber != null) {
                query.append(firstCondition ? " " : " AND ");
                firstCondition = false;
                query.append(field.getSqlFieldName() + " <= ?");
            }
            if (fromTime != null) {
                query.append(firstCondition ? " " : " AND ");
                firstCondition = false;
                query.append(field.getSqlFieldName() + " > ?");
            }
            if (toTime != null) {
                query.append(firstCondition ? " " : " AND ");
                firstCondition = false;
                query.append(field.getSqlFieldName() + " < ?");
            }
        }
        
        if (sortFields.size() > 0) {
            query.append(" ORDER BY " + sortFields.get(0).getSqlFieldName() + " " + sortOrders.get(0));
            for (int i = 1; i < sortFields.size(); i++) {
                query.append(", " + sortFields.get(i).getSqlFieldName() + " " + sortOrders.get(i));
            }
        }
        
        query.append(" LIMIT ?, ?");
        PreparedStatement st = connection.prepareStatement(query.toString());

        int index = 0;
        if (searchString != null) {
            st.setString(++index, "%" + searchString + "%");
        }
        if (fromNumber != null) {
            st.setInt(++index, fromNumber);
        }
        if (toNumber != null) {
            st.setInt(++index, toNumber);
        }
        if (fromTime != null) {
            Timestamp start = new Timestamp(fromTime.getTimeInMillis());
            st.setTimestamp(++index, start);
        }
        if (toTime != null) {
            Timestamp end = new Timestamp(toTime.getTimeInMillis());
            st.setTimestamp(++index, end);
        }

        st.setInt(++index, (pageNumber - 1) * pageSize);
        st.setInt(++index, pageSize);
        return st;
    }
}
