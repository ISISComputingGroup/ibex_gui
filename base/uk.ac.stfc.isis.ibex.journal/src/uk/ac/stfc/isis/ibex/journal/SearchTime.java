
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
import java.util.Optional;

/**
 * Handles parameters for searching for a string and constructing the SQL query
 * .
 */
public class SearchTime extends JournalSearch {
    private Optional<Calendar> fromTime;
    private Optional<Calendar> toTime;

    /**
     * @param field
     *            the journal field to search
     * @param fromTime
     *            the time to search from
     * @param toTime
     *            the time to search to
     */
    public SearchTime(JournalField field, Optional<Calendar> fromTime, Optional<Calendar> toTime) {
        super(field);
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    @Override
    public String createWhereTemplate() {
        StringBuilder query = new StringBuilder();
        if (fromTime.isPresent() || toTime.isPresent()) {
            query.append(" WHERE ");

            ArrayList<String> parts = new ArrayList<String>();

            fromTime.ifPresent(x -> parts.add(field.getSqlFieldName() + " >= ?"));
            toTime.ifPresent(x -> parts.add(field.getSqlFieldName() + " <= ?"));

            query.append(String.join(" AND ", parts));
        }

        return query.toString();
    }

    @Override
    protected PreparedStatement fillTemplate(Connection connection, String query, int pageNumber, int pageSize)
            throws SQLException {
        PreparedStatement st = connection.prepareStatement(query.toString());
        int index = 0;

        if (fromTime.isPresent()) {
            st.setTimestamp(++index, new Timestamp(fromTime.get().getTimeInMillis()));
        }
        if (toTime.isPresent()) {
            st.setTimestamp(++index, new Timestamp(toTime.get().getTimeInMillis()));
        }

        st.setInt(++index, (pageNumber - 1) * pageSize);
        st.setInt(++index, pageSize);
        return st;
    }

}
