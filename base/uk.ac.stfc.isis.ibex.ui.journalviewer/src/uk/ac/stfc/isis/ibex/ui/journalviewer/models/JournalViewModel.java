 /*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2018 Science & Technology Facilities Council.
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
import java.util.EnumSet;
import java.util.List;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.ac.stfc.isis.ibex.journal.JournalModel;
import uk.ac.stfc.isis.ibex.journal.JournalRow;
import uk.ac.stfc.isis.ibex.journal.JournalField;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The viewmodel providing data for the journal viewer view.
 */
public class JournalViewModel extends ModelObject {

    private JournalModel model;
    private String message;
    private List<JournalRow> runs;
    private String lastUpdate;

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
        setPageNumber(model.getPage());
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
     * Gets the runs in the journal.
     * 
     * @return the runs.
     */
    public List<JournalRow> getRuns() {
    	return model.getRuns();
    }
    
    private void setRuns(List<JournalRow> newRuns) {
    	firePropertyChange("runs", null, this.runs = newRuns);
    }
    
    /**
     * Sets a particular journal field to be selected or deselected.
     * @param field an element of the JournalField enum to select
     * @param selected true to select this field, false to deselect it
     */
    public void setFieldSelected(JournalField field, boolean selected) {
    	EnumSet<JournalField> selectedFields = model.getSelectedFields();
    	if (selected) {
    		selectedFields.add(field);
    	} else if (selectedFields.contains(field)) {
    		selectedFields.remove(field);
    	}
    	model.setSelectedFields(selectedFields);
    }
    
    /**
     * Gets whether a particular field is currently selected.
     * @param field the field to get
     * @return true if the field is selected, false otherwise
     */
    public boolean getFieldSelected(JournalField field) {
    	return model.getSelectedFields().contains(field);
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
     */
    public void setPageNumber(int pageNumber) {
    	if (pageNumber != model.getPage()) {
    		int previousPage = model.getPage();
	    	model.setPage(pageNumber);
	    	firePropertyChange("pageNumber", previousPage, model.getPage());
    	}
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

}
