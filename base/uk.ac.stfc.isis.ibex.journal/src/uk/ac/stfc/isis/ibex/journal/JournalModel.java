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
    private Date lastUpdate;

	private List<Map<JournalField, String>> runs = Collections.emptyList();

	private EnumSet<JournalField> selectedFields = EnumSet.of(JournalField.RUN_NUMBER, JournalField.TITLE, JournalField.UAMPS);

    private static final int REFRESH_INTERVAL = 10 * 1000;
    private static final Logger LOG = IsisLog.getLogger(JournalModel.class);
    
    private static final int PAGE_SIZE = 2;
    
    private int pageNumber = 1;
    private int pageMax = 0;

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
                LOG.error("Interrupted while trying to fetch journal data: " + e.getMessage());
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

            setMessage("");
            setLastUpdate(new Date());
            updateRuns(connection);
        } catch (Exception ex) {
            setMessage(Rdb.getError(ex).toString());
            LOG.error(ex);
        } finally {
        	try {
        		connection.close();
        	} catch (SQLException | NullPointerException e) {
				// Do nothing - connection was null, or already closed.
                LOG.warn("Tried closing non-existent connection.");
			}
        }
    }
    
    /**
     * Clears instrument specific data in the model.
     */
    public void clearModel() {
    	setRuns(Collections.<Map<JournalField, String>>emptyList());
        lastUpdate = null;
    }
    
    /**
     * Updates the runs from the database.
     * @param connection - the SQL connection to use
     * @throws SQLException - If there was an error while querying the database
     */
    private void updateRuns(Connection connection) throws SQLException {
    	
    	if (getSelectedFields().size() <= 0) {
    		setRuns(Collections.<Map<JournalField, String>>emptyList());
    		return;
    	}
    	
    	ResultSet rs = connection.createStatement().executeQuery(constructSQLQuery());
    	
    	List<Map<JournalField, String>> runs = new ArrayList<>();
    	
    	while (rs.next()) {
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
    	
    	rs = connection.createStatement().executeQuery(constructCountFieldsSQLQuery());
    	rs.next();
	    setPageMax((int) Math.ceil(rs.getInt(1)/(double) PAGE_SIZE));
    }
    
    /**
     * Constructs the SQL query which extracts all relevant information from the database.
     * @see getSelectedFields for the fields which will be selected.
     * @return the SQL query to be executed.
     */
    private String constructSQLQuery() {
    	
    	Set<JournalField> selectedFields = getSelectedFields();
    	
    	Set<String> selectedFieldNames = new HashSet<String>();
    	for (JournalField field : selectedFields) {
    		selectedFieldNames.add(field.getSqlFieldName());
    	}
    	
    	String fields;
    	if (selectedFieldNames.size() > 0) {
    		fields = String.join(", ", selectedFieldNames.toArray(new String[selectedFieldNames.size()]));
    	} else {
    		fields = "null";
    	}
    	
    	String query = String.format(
    			"SELECT %s FROM journal_entries ORDER BY run_number DESC LIMIT %s, %s", fields, (pageNumber-1) * PAGE_SIZE, PAGE_SIZE);
    	return query;
    }
    
    private String constructCountFieldsSQLQuery() {
    	return "SELECT COUNT(*) FROM journal_entries";
    }
    
    private void setRuns(List<Map<JournalField, String>> runs) {
    	firePropertyChange("runs", this.runs, this.runs = runs);
    }
    
    /**
     * Gets the runs that were extracted from the database.
     * 
     * Format is a List of Maps, where each element in the list is a single run.
     * 
     * The Maps map a JournalField enum to their value as extracted from the database. 
     * A JournalField will only be present in this map if it is selected (@see getSelectedFields).
     * 
     * @return the runs
     */
    public List<Map<JournalField, String>> getRuns() {
    	return runs;
    }
    
    /**
     * Sets the selected fields.
     * @param selected an enumset containing all of the JournalFields which are currently selected
     */
    public void setSelectedFields(EnumSet<JournalField> selected) {
    	this.selectedFields = selected;
    	refresh();
    }
    
    /**
     * Gets the set of fields which are currently selected.
     * @return an enumset instance containing the elements of the JournalField enum which are currently selected
     */
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
    
    /**
     * Sets the page number that is being looked at.
     * 
     * @param page The new page number
     */
    public void setPage(int pageNumber) {
        firePropertyChange("pageNumber", this.pageNumber, this.pageNumber = pageNumber);
        refresh();
    }

    /**
     * Returns the current page number.
     * @return The page number.
     */
    public int getPage() {
        return pageNumber;
    }
    
    /**
     * The maximum page number that can be selected.
     * @param max the new maximum selectable page number
     */
    public void setPageMax(int max) {
    	firePropertyChange("pageNumberMax", this.pageMax, this.pageMax = max);
    }

    /**
     * Gets the maximum page number with data in it.
     * @return the maximum page number
     */
	public int getPageMax() {
		return pageMax;
	}
}
