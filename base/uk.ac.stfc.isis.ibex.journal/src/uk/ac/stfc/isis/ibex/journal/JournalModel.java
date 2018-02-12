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

import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;

import uk.ac.stfc.isis.ibex.databases.Rdb;
import uk.ac.stfc.isis.ibex.journal.preferences.PreferenceConstants;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Fetches and stores journal data from a SQL database.
 */
public class JournalModel extends ModelObject implements Runnable {

    IPreferenceStore preferenceStore;

    private String message = "";
    private boolean connectionSuccess = false;
    private Date lastUpdate;

    private static final int REFRESH_INTERVAL = 10 * 1000;
    private static final Logger LOG = IsisLog.getLogger(JournalModel.class);

	/**
	 * Constructor for the journal model. Takes a preferenceStore as an argument
	 * from which to extract the journal db details.
	 * 
	 * @param preferenceStore The preference store
	 */
    public JournalModel(IPreferenceStore preferenceStore) {
        this.preferenceStore = preferenceStore;
        Thread thread = new Thread(this);
        thread.start();

        // TODO get last update
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
                LOG.error("Interrupted while trying to fetch journal data: " + e.getMessage());
            }
        }
    }

    /**
     * Attempts to connect to the database and updates the status accordingly.
     */
    public void refresh() {
        try {
            String schema = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_SCHEMA);
            String user = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_USERNAME);
            String password = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_PASSWORD);

            Rdb rdb = Rdb.connectToDatabase(schema, user, password);

            setConnectionSuccess(true);
            setMessage("");
            setLastUpdate(new Date());

        } catch (Exception ex) {
            setConnectionSuccess(false);
            setMessage(Rdb.getError(ex).toString());
            LOG.error(ex);
        }
    }

    /**
     * Sets the connection status message to display.
     * 
     * @param message The message
     */
    public void setMessage(String message) {
        firePropertyChange("message", this.message, this.message = message);
    }

    /**
     * @return The connection status message
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Sets the current connection status.
     * 
     * @param connectionSuccess The connection status
     */
    public void setConnectionSuccess(boolean connectionSuccess) {
        firePropertyChange("connectionSuccess", this.connectionSuccess, this.connectionSuccess = connectionSuccess);
    }

    /**
     * @return The connection status
     */
    public boolean getConnectionSuccess() {
        return this.connectionSuccess;
    }

    /**
     * Sets the date of the last successful update.
     * 
     * @param lastUpdate
     *            The date of the last update
     */
    public void setLastUpdate(Date lastUpdate) {
        firePropertyChange("lastUpdate", this.lastUpdate, this.lastUpdate = lastUpdate);
    }

    /**
     * @return The date of the last successful update.
     */
    public Date getLastUpdate() {
        return this.lastUpdate;
    }
}
