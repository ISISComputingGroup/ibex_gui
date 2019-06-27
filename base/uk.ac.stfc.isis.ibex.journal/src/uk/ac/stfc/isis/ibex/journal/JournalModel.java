 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2019 Science & Technology Facilities Council.
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;

import java.sql.PreparedStatement;

import uk.ac.stfc.isis.ibex.databases.Rdb;
import uk.ac.stfc.isis.ibex.journal.preferences.PreferenceConstants;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Fetches and stores journal data from a SQL database.
 */
public class JournalModel extends ModelObject {

    IPreferenceStore preferenceStore;

    private String message = "";
    private Date lastUpdate;

	private List<JournalRow> runs = Collections.emptyList();

	private EnumSet<JournalField> selectedFields = EnumSet.of(JournalField.RUN_NUMBER, JournalField.TITLE, JournalField.UAMPS);

    private static final Logger LOG = IsisLog.getLogger(JournalModel.class);
    private static final int PAGE_SIZE = 25;
    
    // In ms. If a query takes longer than this, issue a warning.
    // Exact choice of number is arbitrary.
    private static final int QUERY_DURATION_WARNING_LEVEL = 2000; 
    
    private int pageNumber = 1;
    private int pageMax = 1;
    
    // The search parameters of the most recent or active search
    // Used so the search is remembered when changing the page number
    private JournalField activeField = JournalField.RUN_NUMBER;
    private String activeSearchString = null;
    private Integer activeFromNumber = null;
    private Integer activeToNumber = null;
    private Calendar activeFromTime = null;
    private Calendar activeToTime = null;
    private ArrayList<JournalField> sortFields = new ArrayList<JournalField>();
    private ArrayList<String> sortOrders = new ArrayList<String>();

	/**
	 * Constructor for the journal model. Takes a preferenceStore as an argument
	 * from which to extract the journal db details.
	 * 
	 * @param preferenceStore The preference store
	 */
    public JournalModel(IPreferenceStore preferenceStore) {
        this.preferenceStore = preferenceStore;
        sortFields.add(JournalField.RUN_NUMBER);
        sortOrders.add("DESC");
    }

    /**
     * Attempts to connect to the database and updates the status accordingly.
     */
    public void refresh() {
    	search(activeField, activeSearchString, activeFromNumber, activeToNumber, activeFromTime, activeToTime);
    }
    
    /**
     * Attempts to connect to the database and update the status to display
     * runs that match the request parameters.
     * 
     * @param field The journal field to search by.
     * @param searchString Search the 'searchField' field of every record for this string value (null = no string search)
     * @param fromNumber Consider only runs with a run number from this number and up (null = no limit)
     * @param toNumber Consider only runs with a run number from this number and below (null = no limit)
     * @param fromTime Consider only runs with a start time after this time (null = no limit).
     * @param toTime Consider only runs with a start time before this time (null = no limit).
     */
    public void search(JournalField field, String searchString, Integer fromNumber, Integer toNumber, Calendar fromTime,
            Calendar toTime) {
        Connection connection = null;
        try {
            String schema = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_SCHEMA);
            String user = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_USERNAME);
            String password = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_PASSWORD);

            connection = Rdb.connectToDatabase(schema, user, password).getConnection();

            setMessage("");
            setLastUpdate(new Date());
            updateRunsSearch(connection, field, searchString, fromNumber, toNumber, fromTime, toTime);
            setActiveParameters(field, searchString, fromNumber, toNumber, fromTime, toTime);
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
    	setRuns(Collections.<JournalRow>emptyList());
        lastUpdate = null;
    }
    
    /**
     * Searches for and updates runs form the database.
     * 
     * @param connection the SQL connection to use.
     * @param field The journal field to search by.
     * @param searchString Search the 'searchField' field of every record for this string value (null = no string search)
     * @param fromNumber Consider only runs with a run number from this number and up (null = no limit)
     * @param toNumber Consider only runs with a run number from this number and below (null = no limit)
     * @param fromTime Consider only runs with a start time after this time (null = no limit).
     * @param toTime Consider only runs with a start time before this time (null = no limit).
     * @throws SQLException - If there was an error while querying the database
     */
    private void updateRunsSearch(Connection connection, JournalField field, String searchString, Integer fromNumber,
            Integer toNumber, Calendar fromTime, Calendar toTime) throws SQLException{
        long startTime = System.currentTimeMillis();

//        ResultSet rs = constructSearchSQLQuery(connection, field, searchString, fromNumber, toNumber, fromTime, toTime).executeQuery();
        
        JournalSqlStatement journalStatement = new JournalSqlStatement(pageNumber, PAGE_SIZE);
        ResultSet rs = journalStatement.constructSearchQuery(connection, field, searchString, fromNumber, toNumber, fromTime, toTime, sortFields, sortOrders).executeQuery();
        
        List<JournalRow> runs = new ArrayList<>();
        while (rs.next()) {
            JournalRow run = new JournalRow();
            for (JournalField property : selectedFields) {
                try {
                    String sqlValue = rs.getString(property.getSqlFieldName());
                    String value = property.getFormatter().format(sqlValue);
                    run.put(property, value);
                } catch (SQLException e) {
                    run.put(property, "None");
                }
            }
            runs.add(run);
        }
        rs.close();
        
        setRuns(Collections.unmodifiableList(runs));
        updateRunsCount(connection);
        
        long totalTime = System.currentTimeMillis() - startTime;
        if (totalTime > QUERY_DURATION_WARNING_LEVEL) {
            LOG.warn(String.format(
                    "Journal parser SQL queries took %s ms. Should have taken less than %s ms.",
                    totalTime, QUERY_DURATION_WARNING_LEVEL));
        }
    }
    
    private void updateRunsCount(Connection connection) throws SQLException {
    	ResultSet rs = constructCountFieldsSQLQuery(connection).executeQuery();
    	if (!rs.next()) {
    		throw new RuntimeException("No results returned from SQL query to count rows.");
    	}
	    setTotalResults(rs.getInt(1));
	    rs.close();
    }
    
    /**
     * Constructs a SQL query which gets the number of runs available.
     * 
     * NOTE: Ensure that arbitrary strings cannot be inserted into this query, maliciously or 
     * accidentally as that could lead to a SQL injection vulnerability.
     * 
     * @return a SQL prepared statement ready to be executed.
     * @throws SQLException if a SQL exception occurred while preparing the statement
     */
    private PreparedStatement constructCountFieldsSQLQuery(Connection connection) throws SQLException {
    	return connection.prepareStatement("SELECT COUNT(*) FROM journal_entries");
    }
    
    /**
     * Sets the active search parameters.
     * 
     * @param field
     * @param searchString
     * @param fromNumber
     * @param toNumber
     * @param fromTime
     * @param toTime
     */
    private void setActiveParameters(JournalField field, String searchString, Integer fromNumber,
            Integer toNumber, Calendar fromTime, Calendar toTime) {
        activeField = field;
        activeSearchString = searchString;
        activeFromNumber = fromNumber;
        activeToNumber = toNumber;
        activeFromTime = fromTime;
        activeToTime = toTime;
    }
    
    /**
     * Resets the active search parameters to their default values.
     */
    public void resetActiveParameters() {
        activeField = JournalField.RUN_NUMBER;
        activeSearchString = null;
        activeFromNumber = null;
        activeToNumber = null;
        activeFromTime = null;
        activeToTime = null;
    }
    
    private void setRuns(List<JournalRow> runs) {
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
    public List<JournalRow> getRuns() {
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
     * @param pageNumber The new page number.
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
    
    private void setTotalResults(int totalResults) {
    	setPageMax((int) Math.ceil(totalResults / (double) PAGE_SIZE));
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
