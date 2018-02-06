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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.journalviewer.models;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.journal.Journal;
import uk.ac.stfc.isis.ibex.journal.JournalModel;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class JournalViewModel extends ModelObject {

    private final static Color NEUTRAL_COLOR = SWTResourceManager.getColor(SWT.COLOR_BLACK);
    private final static Color ERROR_COLOR = SWTResourceManager.getColor(SWT.COLOR_RED);

    private JournalModel model;
    private Color color;
    private String message;

    PropertyChangeListener listener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            update();
        }
    };

    public JournalViewModel() {
        model = Journal.getInstance().getModel();
        model.addPropertyChangeListener(listener);
    }

    private void update() {
        if (model.getConnectionSuccess()) {
            setMessage("Last refresh: " + model.getMessage());
            setColor(NEUTRAL_COLOR);
        } else {
            setMessage("Error retrieving data: " + model.getMessage());
            setColor(ERROR_COLOR);
        }
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        firePropertyChange("message", this.message, this.message = message);
    }


    public Color getColor() {
        return color;
    }

    private void setColor(Color color) {
        firePropertyChange("color", this.color, this.color = color);
    }

}
