 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
 * All rights reserved.
 *
 * This program is distributed in the hope that it will be useful.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution.
 * EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
 * AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
 * OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
 *
 * You should have received a copy of the Eclipse Public License v1.0
 * along with this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or 
 * http://opensource.org/licenses/eclipse-1.0.php
 */

/**
 * 
 */
package uk.ac.stfc.isis.ibex.journal;

import java.sql.SQLRecoverableException;

import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.databases.Rdb;
import uk.ac.stfc.isis.ibex.journal.preferences.PreferenceConstants;

public class JournalModel {

    IPreferenceStore preferenceStore;
    Rdb rdb;

    private static final String CONNECTION_ERROR_MESSAGE = "Error connecting to MySQL database.";
    private static final String UNKNOWN_DATABASE_MESSAGE = "Unknown database name.";
    private static final String ACCESS_DENIED_MESSAGE = "Database access denied.";

    public JournalModel(IPreferenceStore preferenceStore) {
        this.preferenceStore = preferenceStore;
        connect();
    }

    private void connect() {
        try {
            String schema = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_SCHEMA);
            String user = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_USERNAME);
            String password = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_PASSWORD);

            rdb = Rdb.connectToDatabase(schema, user, password);

        } catch (Exception ex) {
            // TODO handle properly
            System.out.println(errorMessage(ex));
        }
    }

    private String errorMessage(Exception ex) {
        if (ex instanceof SQLRecoverableException) {
            return CONNECTION_ERROR_MESSAGE;
        }

        if (ex.getMessage().startsWith("Unknown database")) {
            return UNKNOWN_DATABASE_MESSAGE;
        }

        return ACCESS_DENIED_MESSAGE;
    }

}
