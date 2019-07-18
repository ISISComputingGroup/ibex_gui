
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
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Abstract base class for kinds of journal search.
 */
public abstract class JournalSearch {
    private static final String SELECT = "SELECT * FROM journal_entries";
    protected JournalField field = JournalField.RUN_NUMBER;
    private ArrayList<JournalSort> sorts = new ArrayList<JournalSort>();
    {
        // This is the default sort
        addSort(JournalField.RUN_NUMBER);
    }

    /**
     * @param field
     *            the journal field to search
     */
    public JournalSearch(JournalField field) {
        this.field = field;
    }

    /**
     * Constructs a prepared statement for getting journal data from the
     * database.
     * 
     * @param connection
     *            the SQL connection to use
     * @param pageNumber
     *            the current page number
     * @param pageSize
     *            the number of entries on each page
     * @return An SQL PreparedStatement
     * @throws SQLException
     *             - If there was an error while querying the database
     */
    public PreparedStatement constructQuery(Connection connection, int pageNumber, int pageSize) throws SQLException {
        StringBuilder query = new StringBuilder(SELECT);

        query.append(createWhereTemplate());
        query.append(createSortLimitTemplate());
        return fillTemplate(connection, query.toString(), pageNumber, pageSize);
    }

    /**
     * @return A string containing the WHERE section of the query.
     */
    public abstract String createWhereTemplate();

    /**
     * @param connection
     *            the SQL connection to use
     * @param query
     *            The string query template to fill.
     * @param pageNumber
     *            the current page number
     * @param pageSize
     *            the number of entries on each page
     * @return A full prepared statement ready to be executed.
     * @throws SQLException
     *             - If there was an error while querying the database
     */
    protected abstract PreparedStatement fillTemplate(Connection connection, String query, int pageNumber, int pageSize)
            throws SQLException;

    /**
     * @return A string containing the ORDER BY and LIMIT sections of the query.
     */
    public String createSortLimitTemplate() {
        if (sorts.size() == 0) {
            throw new IllegalStateException("Cannot not sort data.");
        }
        StringBuilder statement = new StringBuilder();
        statement.append(" ORDER BY ");

        statement.append(sorts.stream().map(js -> createOrderByString(js)).collect(Collectors.joining(", ")));

        statement.append(" LIMIT ?, ?");
        return statement.toString();
    }

    /**
     * @param journalSort
     *            a journal sort
     * @return a string to be inserted into the query for ordering with this
     *         journal sort
     */
    private String createOrderByString(JournalSort journalSort) {
        if (journalSort.sortField == JournalField.RB_NUMBER) {
            return "cast(" + journalSort.sortField.getSqlFieldName() + " as unsigned) "
                    + journalSort.direction.getSql();
        } else {
            return journalSort.sortField.getSqlFieldName() + " " + journalSort.direction.getSql();
        }
    }

    /**
     * Adds a field to be used to order the results in the default order.
     * 
     * @param sortField
     *            The field to sort with.
     */
    public void addSort(JournalField sortField) {
        sorts.add(new JournalSort(sortField, sortField.getSortDirection()));
    }

    /**
     * Clear the sorts list.
     */
    public void clearSorts() {
        sorts = new ArrayList<JournalSort>();
    }

    /**
     * @return the first sort in the list of sorts
     */
    public JournalSort getPrimarySort() {
        if (sorts.size() == 0) {
            throw new IllegalStateException("There is no sort");
        }
        return sorts.get(0);
    }

    /**
     * @return the sorts
     */
    public ArrayList<JournalSort> getSorts() {
        return sorts;
    }

    /**
     * @param sorts the sorts to set
     */
    public void setSorts(ArrayList<JournalSort> sorts) {
        this.sorts = sorts;
    }

}
