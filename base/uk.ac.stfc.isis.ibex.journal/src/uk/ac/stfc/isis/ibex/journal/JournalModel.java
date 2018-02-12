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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	private List<Map<JournalField, String>> runs = Collections.emptyList();

	private EnumSet<JournalField> selectedFields = EnumSet.allOf(JournalField.class);

    private final static int REFRESH_INTERVAL = 10 * 1000;
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

    /**
     * Attempts to connect to the database and updates the status accordingly.
     */
    public void refresh() {
    	Connection connection = null;
        try {
            String schema = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_SCHEMA);
            String user = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_USERNAME);
            String password = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_PASSWORD);

            connection = Rdb.connectToDatabase(schema, user, password).getConnection();

            String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
            setConnectionSuccess(true);
            setMessage(timeStamp);
            updateRuns(connection);
        } catch (Exception ex) {
            setConnectionSuccess(false);
            setMessage(Rdb.getError(ex).toString());
            setRuns(Collections.emptyList());
            LOG.error(ex);
        } finally {
        	try {
        		connection.close();
        	} catch (SQLException | NullPointerException e) {
				// Do nothing - connection was null, or already closed.
			}
        }
    }
    
    /**
     * Updates the runs from the database.
     * @param connection - the SQL connection to use
     * @throws SQLException - If there was an error while querying the database
     */
    private void updateRuns(Connection connection) throws SQLException {
    	
    	if (getSelectedFields().size() <= 0) {
    		setRuns(Collections.emptyList());
    		return;
    	}
    	
    	ResultSet rs = connection.createStatement().executeQuery(constructSQLQuery());
    	
    	List<Map<JournalField, String>> runs = new ArrayList<>();
    	
    	while(rs.next()) {
    		Map<JournalField, String> run = new HashMap<>();
    		for (JournalField property : selectedFields) {
    			try {
    				run.put(property, rs.getString(property.getSqlFieldName()));
    			} catch (SQLException e) {
					run.put(property, "None");
				}
    		}  
    		runs.add(Collections.unmodifiableMap(run));
    	}
    	
    	setRuns(Collections.unmodifiableList(runs));
    }
    
    private String constructSQLQuery() {
    	
    	Set<JournalField> selectedFields = getSelectedFields();
    	
    	Set<String> selectedFieldNames = new HashSet<String>();
    	for (JournalField field : selectedFields) {
    		selectedFieldNames.add(field.getSqlFieldName());
    	}
    	
    	String fields;
    	if(selectedFieldNames.size() > 0) {
    		fields = String.join(", ", selectedFieldNames.toArray(new String[selectedFieldNames.size()]));
    	} else {
    		fields = "null";
    	}
    	
    	String query = String.format("SELECT %s FROM journal_entries ORDER BY run_number DESC LIMIT 1000", fields);
    	return query;
    }
    
    private void setRuns(List<Map<JournalField, String>> runs) {
    	firePropertyChange("runs", this.runs, this.runs = runs);
    }
    
    public List<Map<JournalField, String>> getRuns() {
    	return runs;
    }
    
    public void setSelectedFields(EnumSet<JournalField> selected) {
    	this.selectedFields = selected;
    	refresh();
    }
    
    public EnumSet<JournalField> getSelectedFields() {
    	return selectedFields;
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

}
