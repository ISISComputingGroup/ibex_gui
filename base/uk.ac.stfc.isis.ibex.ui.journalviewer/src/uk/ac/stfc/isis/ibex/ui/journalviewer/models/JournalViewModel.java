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
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.ac.stfc.isis.ibex.journal.JournalModel;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The viewmodel providing data for the journal viewer view.
 */
public class JournalViewModel extends ModelObject {

    private JournalModel model;
    private String message;
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

    private boolean isToday(Date date) {
        System.out.println("Date is " + date);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date today = calendar.getTime();
        return date.after(today);
    }
}
