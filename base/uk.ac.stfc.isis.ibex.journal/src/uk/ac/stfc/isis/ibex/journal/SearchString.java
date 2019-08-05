
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

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Handles parameters for searching for a string and constructing the SQL query
 * .
 */
public class SearchString extends JournalSearch {
    private String searchString;

    /**
     * @param field
     *            the journal field to search
     * @param searchString
     *            the string to search for
     */
    public SearchString(JournalField field, String searchString) {
        super(field);
        this.searchString = searchString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createWhereTemplate() {
        return " WHERE " + field.getSqlFieldName() + " LIKE ?";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PreparedStatement fillTemplate(PreparedStatement statement) throws SQLException {
        statement.setString(1, "%" + searchString + "%");
        return statement;
    }

}
