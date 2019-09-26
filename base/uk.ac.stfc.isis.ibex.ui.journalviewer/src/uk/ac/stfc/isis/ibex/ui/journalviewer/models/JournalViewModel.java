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
import org.eclipse.swt.widgets.DateTime;

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
    
    enum DateTimeValidation {
        TRUE,
        FALSE,
        EQUAL
    }
    private JournalModel model;
    private String message;
    private List<JournalRow> runs;
    private String lastUpdate;
    private int toNumber = 0;
    private int fromNumber = 0;
    private String noError = "                                                                                  ";
    private String runNumberError = "Run number in incorrect order";
    private String dateTimeError  = "Date/Time in wrong order";
    private String errorMessage = noError;
    private DateTime fromDate, toDate, fromTime, toTime;
    private Boolean setButtonEnable = true;

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
        setRuns(model.getRuns());
        setPageNumberMax(model.getPageMax());
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
     * @param field an element of the JournalField enum to select
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
     * @param pageNumber The number of the results page.
     * @return a CompletableFuture
     */
    public CompletableFuture<Void> setPageNumber(int pageNumber) {
        return model.setPage(pageNumber);
    }
    
    /**
     * @return The current journal entries page.
     */
    public int getPageNumber() {
    	return model.getPage();
    }
    
    /**
     * @param max The maximum number of pages supported by the journal view.
     */
    public void setPageNumberMax(int max) {
	    firePropertyChange("pageNumberMax", 0, model.getPageMax());
    }
    
    /**
     * @return The maximum number of pages supported by the journal view.
     */
    public int getPageNumberMax() {
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
     * @param field The field to sort by
     * @return a CompletableFuture
     */
    public CompletableFuture<Void> sortBy(JournalField field) {
        return model.sortBy(field);
    }

    /**
     * set new end number
     * @param ToNumber
     */
    public void setToNumber(int toNumber) {
        this.toNumber = toNumber;
        String errorMessage = noError;
        if (toNumber < fromNumber) {
            errorMessage = runNumberError;
            
        }
        setErrorMessage(errorMessage);
        
    }

    /**
     * set new from number
     * @param fromNumber new from number
     */
    public void setFromNumber(int fromNumber) {
        this.fromNumber = fromNumber;
        String errorMessage = noError;
        if (toNumber < fromNumber) {
            errorMessage = runNumberError;
            
        }
        setErrorMessage(errorMessage);
        
    }

    /**
     * set error message
     * @return error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * sets the new error message
     * @param message new message
     */
    public void setErrorMessage(String message) {
        firePropertyChange("errorMessage", this.errorMessage, this.errorMessage = message);
        setEnableDisableButton();
    }

    /**
     * decides whether the error message needs to be displayed or not
     * @return the new error message
     */
    public String GetErrorMessageForDateTime() {
        String errorMessage = dateTimeError;
        if (validateYear() == DateTimeValidation.TRUE) {
            errorMessage = noError;
        } else if ((validateYear() == DateTimeValidation.EQUAL) && (validateTime() == DateTimeValidation.TRUE)) {
            errorMessage = noError;
        }
        return errorMessage;

    }
    /**
     * update to new start date
     * @param fromDate new start date
     */
    public void setFromDate(DateTime fromDate) {
        this.fromDate = fromDate;
        String errorMessage = GetErrorMessageForDateTime();
        setErrorMessage(errorMessage);
    }


    /**
     * update to new end date
     * @param toDate new end date
     */
    public void setToDate(DateTime toDate) {
        this.toDate = toDate;
        String errorMessage = GetErrorMessageForDateTime();
        setErrorMessage(errorMessage);
    }

    /**
     * update to new start date
     * @param fromTime new start time
     */
    public void setFromTime(DateTime fromTime) {
        this.fromTime = fromTime;
        String errorMessage = GetErrorMessageForDateTime();
        setErrorMessage(errorMessage);
    }

    /**
     * update new end time
     * @param toTime new to time for start time
     */

    public void setToTime(DateTime toTime) {
        this.toTime = toTime;
        String errorMessage = GetErrorMessageForDateTime();
        setErrorMessage(errorMessage);
    }

    /**
     * validate whether the date is the right way or not
     * 
     * @return true if the date is entered right way
     */
    public DateTimeValidation validateYear() {

        DateTimeValidation retVal = DateTimeValidation.TRUE;
        int fromYear = fromDate.getYear(), fromMonth = fromDate.getYear(), fromDay = fromDate.getDay();
        int toYear = toDate.getYear(), toMonth = toDate.getYear(), toDay = toDate.getDay();

        int fromDateTotal = 12 * fromYear + fromMonth;
        int toDateTotal = 12 * toYear + toMonth;

        if (fromDateTotal > toDateTotal) {
            retVal = DateTimeValidation.FALSE;
        } else if ((fromDateTotal == toDateTotal) && (fromDay == toDay)) {
            retVal = DateTimeValidation.EQUAL;
        } else if ((fromDateTotal == toDateTotal) && (fromDay > toDay)) {
            retVal = DateTimeValidation.FALSE;
        }
        return retVal;
    }


    /**
     * sets start date and time
     * @param fromDate sets start date
     * @param fromTime set start time
     */
    public void setInitialFromDateTime(DateTime fromDate, DateTime fromTime) {
        this.fromTime = fromTime;
        this.fromDate = fromDate;
    }

    /**
     * sets end date and time  
     * @param toDate end date
     * @param toTime end time
     */
    public void setInitialToDateTime(DateTime toDate, DateTime toTime) {
        this.toDate = toDate;
        this.toTime = toTime;
    }

    /**
     * checks whether the time is in correct order
     * @return true or false 
     */
    public DateTimeValidation validateTime() {

        DateTimeValidation retVal = DateTimeValidation.TRUE;

        int fromHour = fromTime.getHours(), fromMinutes = fromTime.getMinutes(), fromSeconds = fromTime.getSeconds();
        int toHour = toTime.getHours(), toMinutes = toTime.getMinutes(), toSeconds = toTime.getSeconds();

        int hoursInSeconds   = 3600;
        int minutesInSeconds = 60;

        int fromTimeInSeconds = (hoursInSeconds * fromHour) + (minutesInSeconds * fromMinutes) + fromSeconds;
        int toTimeInSeconds   = (hoursInSeconds * toHour) + (minutesInSeconds * toMinutes) + toSeconds;

        if (fromTimeInSeconds > toTimeInSeconds) {
            retVal = DateTimeValidation.FALSE;
        }
        return retVal;
    }

    /**
     * Clears Error message on the screen
     */
    public void clearMessage() {
        if (this.message != noError) {
            setErrorMessage(noError);
            
        }
    }

    /**
     * checks if button needs to be enabled or disabled
     */
    public void setEnableDisableButton() {
        Boolean val = true;

        if (this.errorMessage != noError) {
            val = false;
        }
        firePropertyChange("enableDisableButton", this.setButtonEnable, this.setButtonEnable = val);
    }
    /**
     * @return if button needs to be enabled or disabled
     */
    public Boolean getEnableDisableButton() {
        return this.setButtonEnable;
    }
}
