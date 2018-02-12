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
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.journal.JournalModel;
import uk.ac.stfc.isis.ibex.journal.JournalField;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The viewmodel providing data for the journal viewer view.
 */
public class JournalViewModel extends ModelObject {

    /**
     * A neutral color for the status message text.
     */
    private static final Color NEUTRAL_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

    /**
     * A color indicating an error for the status message text.
     */
    private static final Color ERROR_COLOR = Display.getDefault().getSystemColor(SWT.COLOR_RED);

    private JournalModel model;
    private Color color;
    private String message;
    private List<Map<JournalField, String>> runs;

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
        if (model.getConnectionSuccess()) {
            setMessage("Last refresh: " + model.getMessage());
            setColor(NEUTRAL_COLOR);
        } else {
            setMessage("Error retrieving data: " + model.getMessage());
            setColor(ERROR_COLOR);
        }
        setRuns(model.getRuns());
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
     * @return The color indicating the current connection status.
     */
    public Color getColor() {
        return color;
    }

    private void setColor(Color color) {
        firePropertyChange("color", this.color, this.color = color);
    }
    
    /**
     * Gets a string representation of the runs in the journal.
     * 
     * @deprecated This function will be replaced in ticket 2905 
     * @see https://github.com/ISISComputingGroup/IBEX/issues/2905
     * 
     * @return a string representation of the runs.
     */
    public String getRuns() {
    	StringBuilder sb = new StringBuilder();
    	for (Map<JournalField, String> run : runs) {
    		List<JournalField> keys = new ArrayList<>(run.keySet());;
    		Collections.sort(keys);
    		for (JournalField field : keys) {
    			sb.append(field.getFriendlyName() + " = " + run.get(field) + ", ");
    		}
    		sb.append("\n");
    	}
    	return sb.toString();
    }
    
    private void setRuns(List<Map<JournalField, String>> newRuns) {
    	firePropertyChange("runs", this.runs, this.runs = newRuns);
    }
    
    /**
     * Sets a particular journal field to be selected or deselected
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
     * Gets whether a particular field is currently selected
     * @param field the field to get
     * @return true if the field is selected, false otherwise
     */
    public boolean getFieldSelected(JournalField field) {
    	return model.getSelectedFields().contains(field);
    }
}
