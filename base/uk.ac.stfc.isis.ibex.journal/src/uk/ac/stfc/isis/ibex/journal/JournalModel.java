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

package uk.ac.stfc.isis.ibex.journal;

import java.sql.SQLRecoverableException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.databases.DbError;
import uk.ac.stfc.isis.ibex.databases.Rdb;
import uk.ac.stfc.isis.ibex.journal.preferences.PreferenceConstants;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class JournalModel extends ModelObject implements Runnable {

    IPreferenceStore preferenceStore;

    private String message = "";
    private boolean connectionSuccess = false;

    private final static int REFRESH_INTERVAL = 60 * 1000;

    public JournalModel(IPreferenceStore preferenceStore) {
        this.preferenceStore = preferenceStore;
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Periodically tries to fetch new data from the database.
     */
    @Override
    public void run() {
        while (true) {
            refresh();
            try {
                Thread.sleep(REFRESH_INTERVAL);
            } catch (InterruptedException e) {
                // e.printStackTrace();
            }
        }
    }

    public void refresh() {
        try {
            String schema = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_SCHEMA);
            String user = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_USERNAME);
            String password = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_PASSWORD);

            Rdb rdb = Rdb.connectToDatabase(schema, user, password);

            String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
            setConnectionSuccess(true);
            setMessage(timeStamp);

        } catch (Exception ex) {
            setConnectionSuccess(false);
            setMessage(getError(ex).toString());
        }
    }

    private DbError getError(Exception ex) {
        if (ex instanceof SQLRecoverableException) {
            return DbError.CONNECTION_ERROR;
        }
        if (ex.getMessage().startsWith("Unknown database")) {
            return DbError.UNKNOWN_DB;
        }
        return DbError.ACCESS_DENIED;
    }

    public void setMessage(String message) {
        firePropertyChange("message", this.message, this.message = message);
    }

    public String getMessage() {
        return this.message;
    }

    public void setConnectionSuccess(boolean connectionSuccess) {
        firePropertyChange("connectionSuccess", this.connectionSuccess, this.connectionSuccess = connectionSuccess);
    }

    public boolean getConnectionSuccess() {
        return this.connectionSuccess;
    }

}
