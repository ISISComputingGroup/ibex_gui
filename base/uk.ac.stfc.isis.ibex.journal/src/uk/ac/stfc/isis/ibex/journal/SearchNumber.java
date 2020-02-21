
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
import java.util.ArrayList;
import java.util.OptionalInt;

/**
 * Handles parameters for searching between two integers and constructing the
 * SQL query.
 */
public class SearchNumber extends JournalSearch {
    private OptionalInt fromNumber;
    private OptionalInt toNumber;
    
    /**
     * @param field
     *            the journal field to search
     * @param fromNumber
     *            the number to search from
     * @param toNumber
     *            the number to search to
     */
    public SearchNumber(JournalField field, OptionalInt fromNumber, OptionalInt toNumber) {
        super(field);
        this.fromNumber = fromNumber;
        this.toNumber = toNumber;
    }

    @Override
    public String createWhereTemplate() {
        StringBuilder query = new StringBuilder();
        if (fromNumber.isPresent() || toNumber.isPresent()) {
            query.append(" WHERE ");
            
            ArrayList<String> parts = new ArrayList<String>();
            
            fromNumber.ifPresent(x -> parts.add(field.getSqlFieldName() + " >= ?"));
            toNumber.ifPresent(x -> parts.add(field.getSqlFieldName() + " <= ?"));
            
            query.append(String.join(" AND ", parts));
        }
        
        return query.toString();
    }

    @Override
    protected PreparedStatement fillTemplate(PreparedStatement statement) throws SQLException {
        int index = 0;

        if (fromNumber.isPresent()) {
            statement.setInt(++index, fromNumber.getAsInt());
        }
        if (toNumber.isPresent()) {
            statement.setInt(++index, toNumber.getAsInt());
        }

        return statement;
    }

}
