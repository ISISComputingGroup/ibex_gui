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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.journalviewer.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.swt.SWT;

import uk.ac.stfc.isis.ibex.journal.JournalField;
import uk.ac.stfc.isis.ibex.journal.JournalModel;
import uk.ac.stfc.isis.ibex.journal.JournalRow;
import uk.ac.stfc.isis.ibex.journal.JournalSearch;
import uk.ac.stfc.isis.ibex.journal.JournalSort.JournalSortDirection;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The viewmodel providing data for the journal viewer view.
 */
public class JournalViewModel extends ModelObject {

	private JournalModel model;
	private String message;
	private List<JournalRow> runs;
	private String lastUpdate;
	private String resultsInfo;
	private int pageMax;
	private int pageNumber = 1;
	private int toNumber = 0;
	private int fromNumber = 0;
	private static final String NO_ERROR = "";
	private static final String RUN_NUMBER_ERROR = "Run number in incorrect order";
	private static final String DATE_TIME_ERROR = "Date/Time in wrong order";
	private String errorMessage = NO_ERROR;
	private Calendar fromDateTime;
	private Calendar toDateTime;
	private boolean searchButtonEnabled = true;
	private boolean btnPrevPageEnabled = true;
	private boolean btnNextPageEnabled = true;

	PropertyChangeListener listener = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			update();
		}
	};

	/**
	 * Constructor for the viewmodel. Takes a model of the journal backend to
	 * observe.
	 * 
	 * @param model The backend model
	 */
	public JournalViewModel(JournalModel model) {
		this.model = model;
		this.model.addPropertyChangeListener(listener);
		update();
	}

	private void update() {
		setLastUpdate("Last successful update: " + dateToString(model.getLastUpdate()));
		setMessage(model.getMessage());
		setRuns(getRuns());
		setPageMax(getPageMax());
		updatePageNumber(getPageNumber());
		setResultsInfo(getResultsInfo());
		enableOrDisablePageControlButtons();
	}

	/**
	 * @return The connection status message.
	 */
	public String getMessage() {
		return message;
	}

	private void setMessage(String message) {
		firePropertyChange("message", this.message, this.message = message);
	}

	/**
	 * @return The connection status message.
	 */
	public String getLastUpdate() {
		return lastUpdate;
	}

	private void setLastUpdate(String lastUpdate) {
		firePropertyChange("lastUpdate", this.lastUpdate, this.lastUpdate = lastUpdate);
	}

	/**
	 * @return The current number of result entries.
	 */
	public int getResultsNumber() {
		return model.getResultsNumber();
	}
	
	/**
	 * Returns the number of total current results and the currently displayed results depending on page and page size.
	 * @return The number of total results and currently displayed entries.
	 */
	public String getResultsInfo() {
		int entryFrom;
		int entryTo;
	
		if (model.getResultsNumber() != 0 && model.getPageSize() != 0) {
	    	entryFrom = (model.getPage() - 1) * model.getPageSize() + 1;
	    	entryTo = entryFrom + model.getPageSize() - 1;
	    	if (entryTo > model.getResultsNumber()) { 
	    		entryTo = model.getResultsNumber(); 
	    		}
		} else {
			entryFrom = entryTo = 0;
		}
	
		return String.format("%d-%d of %d", entryFrom, entryTo, model.getResultsNumber());
	}
	
	private void setResultsInfo(String resultsInfo) {
		firePropertyChange("resultsInfo", this.resultsInfo, this.resultsInfo = resultsInfo);
	}

	/**
	 * Refreshes the data from the journal database.
	 */
	public void refresh() {
		model.refresh();
	}

	/**
	 * Resets the active search to empty.
	 */
	public void resetActiveSearch() {
		model.resetActiveSearch();
	}

	/**
	 * @return the active search
	 */
	public JournalSearch getActiveSearch() {
		return model.getActiveSearch();
	}

	/**
	 * @param search the search parameters to make active
	 */
	public void setActiveSearch(JournalSearch search) {
		model.setActiveSearch(search);
	}

	/**
	 * Gets the runs in the journal.
	 * 
	 * @return the runs.
	 */
	public List<JournalRow> getRuns() {
		return model.getRuns();
	}

	private void setRuns(List<JournalRow> newRuns) {
		firePropertyChange("runs", this.runs, this.runs = newRuns);
	}

	/**
	 * Sets a particular journal field to be selected or deselected.
	 * 
	 * @param field    an element of the JournalField enum to select
	 * @param selected true to select this field, false to deselect it
	 * @return a CompletableFuture
	 */
	public CompletableFuture<Void> setFieldSelected(JournalField field, boolean selected) {
		EnumSet<JournalField> selectedFields = model.getSelectedFields();
		if (selected) {
			selectedFields.add(field);
		} else if (selectedFields.contains(field)) {
			selectedFields.remove(field);
		}
		return model.setSelectedFields(selectedFields);
	}

	/**
	 * Gets whether a particular field is currently selected.
	 * 
	 * @param field the field to get
	 * @return true if the field is selected, false otherwise
	 */
	public boolean getFieldSelected(JournalField field) {
		return model.getSelectedFields().contains(field);
	}

	/**
	 * @return the sort direction of the primary sort for SWT to use
	 */
	public int getSortDirection() {
		if (getActiveSearch().getPrimarySort().getDirection() == JournalSortDirection.DESCENDING) {
			return SWT.DOWN;
		} else {
			return SWT.UP;
		}
	}

	private String dateToString(Date lastUpdate) {
		if (lastUpdate == null) {
			return "N/A";
		}
		if (isToday(lastUpdate)) {
			return DateFormat.getTimeInstance(DateFormat.MEDIUM).format(lastUpdate);
		} else {
			return DateFormat.getDateTimeInstance().format(lastUpdate);
		}
	}

	/**
	 * @param date Is the provided date the same as today's date
	 * @return Whether the given date is the same as today's date
	 */
	private boolean isToday(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date today = calendar.getTime();
		return date.after(today);
	}

	/**
	 * @param pageNumber the current page number
	 */
	public void updatePageNumber(int pageNumber) {
		firePropertyChange("pageNumber", this.pageNumber, this.pageNumber = pageNumber);
	}

	/**
	 * @param pageNumber The number of the results page.
	 * @return a CompletableFuture
	 */
	public CompletableFuture<Void> setPageNumber(int pageNumber) {
		CompletableFuture<Void> future;
		if (pageNumber >= 1 && pageNumber <= model.getPageMax()) {
			future = model.setPageNumber(pageNumber);
		} else {
		    future = new CompletableFuture<Void>();
			future.complete(null);
			setMessage(String.format("Page [%d] is out of bounds", pageNumber));
		}
		
		return future;
	}

	/**
	 * @return The current journal entries page.
	 */
	public int getPageNumber() {
		return model.getPage();
	}
	
	/**
	 * Calls {@link JournalViewModel#setPageNumber(int)} to change the current page to pageNumber.
	 * If the desired page is not available due to it being higher than the maximum page number,
	 * change the current page to the last (max page number) instead.
	 * @param pageNumber The number of the desired results page.
	 * @return a CompletableFuture.
	 */
	public CompletableFuture<Void> goToPage(int pageNumber) {
		return (pageNumber > getPageMax()) ? setPageNumber(getPageMax()) : setPageNumber(pageNumber);
	}
	
	/**
	 * Calls {@link JournalViewModel#setPageNumber(int)} to lower the current page number by 1.
	 * @return a CompletableFuture.
	 */
	public CompletableFuture<Void> prevPage() {
    	int newerPageNumber = model.getPage() - 1;
    	return setPageNumber(newerPageNumber);
	}
	
	/**
	 * Calls {@link JournalViewModel#setPageNumber(int)} to increase the current page number by 1.
	 * @return a CompletableFuture.
	 */
	public CompletableFuture<Void> nextPage() {
    	int olderPageNumber = model.getPage() + 1;
    	return setPageNumber(olderPageNumber);
	}
	
	/**
	 * Calls {@link JournalViewModel#setPageNumber(int)} to set the current page number to minimum.
	 * @return a CompletableFuture.
	 */
	public CompletableFuture<Void> firstPage() {
    	int newestPageNumber = 1;
    	return setPageNumber(newestPageNumber);
	}
	
	/**
	 * Calls {@link JournalViewModel#setPageNumber(int)} to set the current page number to maximum.
	 * @return a CompletableFuture.
	 */
	public CompletableFuture<Void> lastPage() {
    	int oldestPageNumber = model.getPageMax();
    	return setPageNumber(oldestPageNumber);
	}
	
	/**
	 * @param max The maximum number of pages supported by the journal view.
	 */
	public void setPageMax(int max) {
		firePropertyChange("pageMax", this.pageMax, this.pageMax = max);
	}

	/**
	 * @return The maximum number of pages supported by the journal view.
	 */
	public int getPageMax() {
		return model.getPageMax();
	}

	/**
	 * @return the searchableFields
	 */
	public List<JournalField> getSearchableFields() {
		return model.getSearchableFields();
	}

	/**
	 * Sorts by the specified field, and swaps the direction if already active.
	 * 
	 * @param field The field to sort by
	 * @return a CompletableFuture
	 */
	public CompletableFuture<Void> sortBy(JournalField field) {
		return model.sortBy(field);
	}

	/**
	 * Set new end number.
	 * 
	 * @param toNumber the number to set to value to
	 */
	public void setToNumber(int toNumber) {
		this.toNumber = toNumber;
		String errorMessage = NO_ERROR;
		if (toNumber < fromNumber) {
			errorMessage = RUN_NUMBER_ERROR;

		}
		setErrorMessage(errorMessage);

	}

	/**
	 * Set new from number.
	 * 
	 * @param fromNumber new from number
	 */
	public void setFromNumber(int fromNumber) {
		this.fromNumber = fromNumber;
		String errorMessage = NO_ERROR;
		if (toNumber < fromNumber) {
			errorMessage = RUN_NUMBER_ERROR;

		}
		setErrorMessage(errorMessage);

	}

	/**
	 * Set error message.
	 * 
	 * @return error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Sets the new error message.
	 * 
	 * @param message new message
	 */
	public void setErrorMessage(String message) {
		firePropertyChange("errorMessage", this.errorMessage, this.errorMessage = message);
		setSearchButtonEnabled();
	}

	/**
	 * Decides whether the error message needs to be displayed or not.
	 * 
	 * @return the new error message
	 */
	private String getErrorMessageForDateTime() {
		String errorMessage = DATE_TIME_ERROR;
		if (toDateTime.compareTo(fromDateTime) >= 0) {
			errorMessage = NO_ERROR;
		}
		return errorMessage;

	}

	/**
	 * Update to new start date.
	 * 
	 * @param fromDate new start date
	 */
	public void setFromDate(Calendar fromDate) {
		this.fromDateTime.set(fromDate.get(Calendar.YEAR), fromDate.get(Calendar.MONTH),
				fromDate.get(Calendar.DAY_OF_MONTH));
		String errorMessage = getErrorMessageForDateTime();
		setErrorMessage(errorMessage);
	}

	/**
	 * Update to new end date.
	 * 
	 * @param toDate new end date
	 */
	public void setToDate(Calendar toDate) {
		this.toDateTime.set(toDate.get(Calendar.YEAR), toDate.get(Calendar.MONTH),
				toDate.get(Calendar.DAY_OF_MONTH));
		String errorMessage = getErrorMessageForDateTime();
		setErrorMessage(errorMessage);
	}

	/**
	 * Update to new start date.
	 * 
	 * @param fromTime new start time
	 */
	public void setFromTime(Calendar fromTime) {
		this.fromDateTime.setTime(fromTime.getTime());
		String errorMessage = getErrorMessageForDateTime();
		setErrorMessage(errorMessage);
	}

	/**
	 * Update new end time.
	 * 
	 * @param toTime new to time for start time
	 */

	public void setToTime(Calendar toTime) {
		this.toDateTime.setTime(toTime.getTime());
		String errorMessage = getErrorMessageForDateTime();
		setErrorMessage(errorMessage);
	}

	/**
	 * Sets start date and time.
	 * 
	 * @param fromDate sets start date
	 * @param fromTime set start time
	 */
	public void setInitialFromDateTime(Calendar fromDate, Calendar fromTime) {
		this.fromDateTime = fromTime;
		this.fromDateTime.setTime(fromTime.getTime());
	}

	/**
	 * Sets end date and time.
	 * 
	 * @param toDate end date
	 * @param toTime end time
	 */
	public void setInitialToDateTime(Calendar toDate, Calendar toTime) {
		this.toDateTime = toDate;
		this.toDateTime.setTime(toTime.getTime());
	}

	/**
	 * Clears Error message on the screen.
	 */
	public void clearMessage() {
		if (this.message != NO_ERROR) {
			setErrorMessage(NO_ERROR);

		}
	}

	/**
	 * Checks if one or more of the page control buttons should be disabled or enabled,
	 * depending on the current and maximum page number.
	 */
	private void enableOrDisablePageControlButtons() {
		boolean enableOrDisableVal;
		
		// If on first page, disable Previous (and First) page buttons
		enableOrDisableVal = getPageNumber() != 1;
		firePropertyChange("btnPrevPageEnabled", this.btnPrevPageEnabled, this.btnPrevPageEnabled = enableOrDisableVal);

		// If on last page, disable Next (and Last) page buttons
		enableOrDisableVal = getPageNumber() != getPageMax();
		firePropertyChange("btnNextPageEnabled", this.btnNextPageEnabled, this.btnNextPageEnabled = enableOrDisableVal);
	}

	/**
	 * Gets if the button is currently on enabled or disabled state.
	 * @return if button needs to be enabled or disabled
	 */
	public Boolean getBtnPrevPageEnabled() {
		return this.btnPrevPageEnabled;
	}
	/**
	 * Gets if the button is currently on enabled or disabled state.
	 * @return if button needs to be enabled or disabled
	 */
	public Boolean getBtnNextPageEnabled() {
		return this.btnNextPageEnabled;
	}
	
	/**
	 * Checks if button needs to be enabled or disabled.
	 */
	private void setSearchButtonEnabled() {

		boolean enableOrDisableVal = this.errorMessage == NO_ERROR;

		firePropertyChange("searchButtonEnabled", this.searchButtonEnabled, this.searchButtonEnabled = enableOrDisableVal);
	}

	/**
	 * Gets if the button is currently on enabled or disabled state.
	 * @return if button needs to be enabled or disabled
	 */
	public Boolean getSearchButtonEnabled() {
		return this.searchButtonEnabled;
	}
}
