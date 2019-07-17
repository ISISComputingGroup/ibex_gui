
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

/**
 * Handles parameters for searching for a string and constructing the SQL query .
 */
public class EmptySearch extends JournalSearch {
    
    /**
     * Constructor with no parameters which sets field to run number (the default).
     */
    public EmptySearch() {
        super(JournalField.RUN_NUMBER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createWhereTemplate() {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PreparedStatement fillTemplate(Connection connection, String query, int pageNumber, int pageSize)
            throws SQLException {
        PreparedStatement st = connection.prepareStatement(query.toString());
        st.setInt(1, (pageNumber - 1) * pageSize);
        st.setInt(2, pageSize);
        return st;
    }

}
