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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.Logger;
import org.eclipse.jface.preference.IPreferenceStore;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

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
	
	private static final List<JournalField> SEARCHABLE_FIELDS = Arrays.asList(new JournalField[]{JournalField.RUN_NUMBER,
	        JournalField.TITLE, JournalField.START_TIME, JournalField.RB_NUMBER, JournalField.USERS});

    private static final Logger LOG = IsisLog.getLogger(JournalModel.class);
    private static final int PAGE_SIZE = 25;
    
    // In ms. If a query takes longer than this, issue a warning.
    // Exact choice of number is arbitrary.
    private static final int QUERY_DURATION_WARNING_LEVEL = 2000;
    
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(1, 
			new ThreadFactoryBuilder().setNameFormat("JournalModel-threadpool-%d").build());

    private static final String NO_RESULTS_FOUND = "No results found with current search parameters";
    
    private int pageNumber = 1;
    private int pageMax = 1;
    private int resultsNumber = 0;
    
    // The most recent or active search
    // Used so the search is remembered when changing the page number or refreshing
    private JournalSearch activeSearch = new EmptySearch();

	/**
	 * Constructor for the journal model. Takes a preferenceStore as an argument
	 * from which to extract the journal db details.
	 * 
	 * @param preferenceStore The preference store
	 */
    public JournalModel(IPreferenceStore preferenceStore) {
        this.preferenceStore = preferenceStore;
    }

    /**
     * Reloads the runs with the current parameters.
     * @return a CompleteableFuture
     */
    public CompletableFuture<Void> refresh() {
    	return search(activeSearch);
    }
    
    /**
     * Attempts to connect to the database and update the status to display
     * runs that match the request parameters.
     * 
     * @param search The search to use.
     * @return a CompleteableFuture
     */
    public CompletableFuture<Void> search(JournalSearch search) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        EXECUTOR.submit(() -> {
            Connection connection = null;
            try {
                String schema = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_SCHEMA);
                String user = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_USERNAME);
                String password = preferenceStore.getString(PreferenceConstants.P_JOURNAL_SQL_PASSWORD);

                connection = Rdb.connectToDatabase(schema, user, password).getConnection();

                setMessage("");
                setLastUpdate(new Date());
                searchUpdateRuns(connection, search);

                activeSearch = search;
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
            future.complete(null);
        });
        return future;
    }
    
    /**
     * Clears instrument specific data in the model.
     */
    public void clearModel() {
    	setRuns(Collections.<JournalRow>emptyList());
    	this.updatePageNumber(1);
        lastUpdate = null;
    }
    
    /**
     * Searches for and updates runs from the database.
     * 
     * @param connection the SQL connection to use.
     * @param search The parameters to search with.
     */
    private void searchUpdateRuns(Connection connection, JournalSearch search) throws SQLException {
        long startTime = System.currentTimeMillis();
        
        ResultSet rs = search.constructQuery(connection, pageNumber, PAGE_SIZE).executeQuery();
        
        List<JournalRow> runs = formatResults(rs);
        if (runs.size() ==  0) {
            setMessage(NO_RESULTS_FOUND);
        }
        setRuns(Collections.unmodifiableList(runs));
        updateRunsCount(connection, search);
        long totalTime = System.currentTimeMillis() - startTime;
        if (totalTime > QUERY_DURATION_WARNING_LEVEL) {
            LOG.warn(String.format(
                    "Journal parser SQL queries took %s ms. Should have taken less than %s ms.",
                    totalTime, QUERY_DURATION_WARNING_LEVEL));
        }
    }
    
    /**
     * Formats the result set returned by the database into rows for populating the table.
     * 
     * @param rs The results set generated by a query.
     * @return An array list of journal rows.
     * @throws SQLException
     */
    private List<JournalRow> formatResults(ResultSet rs) throws SQLException {
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
        return runs;
    }
    
    private void updateRunsCount(Connection connection, JournalSearch search) throws SQLException {
    	ResultSet rs = search.constructCountQuery(connection).executeQuery();
    	if (!rs.next()) {
    		throw new RuntimeException("No results returned from SQL query to count rows.");
    	}
    	
    	setPageMax(calcTotalPages(rs.getInt(1)));
    	setResultsNumber(rs.getInt(1));
    	rs.close();
    }
    
    /**
     * Resets the active search to empty.
     */
    public void resetActiveSearch() {
        ArrayList<JournalSort> oldSorts = activeSearch.getSorts();
        activeSearch = new EmptySearch();
        activeSearch.setSorts(oldSorts);
    }
    
    /**
     * @return the active search
     */
    public JournalSearch getActiveSearch() {
        return activeSearch;
    }
    
    /**
     * Sets a new active search. This retains the previous sort.
     * @param search the search to set as active
     */
    public void setActiveSearch(JournalSearch search) {
        search.setSorts(activeSearch.getSorts());
        activeSearch = search;
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
     * @return a CompleteableFuture
     */
    public CompletableFuture<Void> setSelectedFields(EnumSet<JournalField> selected) {
    	this.selectedFields = selected;
    	return refresh();
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
     * Updates the number of the page that is currently being looked at.
     * 
     * @param pageNumber The new page number.
     */
	public void updatePageNumber(int pageNumber) {
		firePropertyChange("pageNumber", this.pageNumber, this.pageNumber = pageNumber);
	}
	
    /**
     * Sets the page number that is being looked at.
     * 
     * @param pageNumber The new page number.
     * @return a CompleteableFuture
     */
	public CompletableFuture<Void> setPageNumber(int pageNumber) {
		updatePageNumber(pageNumber);
		return refresh();
	}
	
	/**
	 * Returns the current page number.
	 * @return The page number.
	 */
	public int getPage() {
	    return pageNumber;
	}
	
	/**
	 * Returns the PAGE_SIZE constant.
	 * @return PAGE_SIZE constant
	 */
	public int getPageSize() {
		return PAGE_SIZE;
	}
	
	/**
	 * Calculates the maximum number of pages.
	 * @param totalResults The number of results.
	 * @return The max page number, lowest is 1.
	 */
	public int calcTotalPages(int totalResults) {
		int returnVal = (int) Math.ceil(totalResults / (double) this.getPageSize());
		return Math.max(returnVal, 1);
	}
	
	/**
	 * Returns the number of entry results from last run.
	 * @return Number of entry results.
	 */
	public int getResultsNumber() {
		return resultsNumber;
	}
	
	/**
	 * Sets the total results number.
	 * 
	 * @param resultsNumber The number of results.
	 */
	private void setResultsNumber(int resultsNumber) {
		firePropertyChange("resultsNumber", this.resultsNumber, this.resultsNumber = resultsNumber);	
	}
	
	/**
	 * The maximum page number that can be selected.
	 * @param max the new maximum selectable page number
	 */
	public void setPageMax(int max) {
		firePropertyChange("pageMax", this.pageMax, this.pageMax = max);
	}
	
	/**
	 * Gets the maximum page number with data in it.
	 * @return the maximum page number
	 */
	public int getPageMax() {
		return pageMax;
	}
	
	/**
	 * @return the searchableFields
	 */
	public List<JournalField> getSearchableFields() {
	    return SEARCHABLE_FIELDS;
	}
	
	/**
	 * Sorts by the specified field, and swaps the direction if already active.
	 * @param field The field to sort by
	 * @return a CompleteableFuture
	 */
	public CompletableFuture<Void> sortBy(JournalField field) {
	    JournalSort primarySort = activeSearch.getPrimarySort();
	    
	    if (primarySort.sortField == field) {
	        primarySort.swapDirection();
	    } else {
	        activeSearch.clearSorts();
	        if (field != JournalField.RUN_NUMBER) {
	            activeSearch.addSort(field);
	        }
	        activeSearch.addSort(JournalField.RUN_NUMBER);
	    }

    return refresh();
}
}
