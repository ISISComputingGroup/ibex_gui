
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
package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

/**
 * Provides the content for file selection menus in the DAE experiment setup
 * tab.
 */
public class DAEComboContentProvider {
    
    String instrumentName;
    String dir;

    /**
     * Constructor. Binds the directory containing the content to display in messages.
     * 
     * @param dir The directory where the content is
     */
    public DAEComboContentProvider(ForwardingObservable<String> dir) {
        this.dir = dir.getValue();
        dir.addObserver(dirAdapter);
        
    }

    /**
     * Returns a string array from a string collection, or an empty array if the
     * input is null.
     * 
     * @param updated the string collection.
     * @return the resulting array.
     */
    private String[] valueOrEmpty(UpdatedValue<Collection<String>> updated) {
    	Collection<String> value = updated != null ? updated.getValue() : null;
        return value != null ? value.toArray(new String[0]) : new String[0];
    }

    /**
     * Gets the list of detector tables currently available to the instrument,
     * or an appropriate error message.
     * 
     * @param list The list of available files to format for displaying.
     * @param pattern The file naming pattern.
     * @return the list of available files.
     */
    public String[] getContent(UpdatedValue<Collection<String>> list, String pattern) {
        String[] tables = valueOrEmpty(list);

        if (list == null) {
            // This happens if the file list PV is not available.
            tables = new String[] {"Error retrieving information from Instrument." };
        } else if (tables.length == 0) {
            // This happens if the file list PV was available but empty.

            // Use a sensible default if the instrument name PV is not available
            // yet.
            String displayDir = this.dir != null ? this.dir : "C:\\Instrument\\Setting\\Config\\<instrument>\\...";

            tables = new String[] {"None found in " + displayDir + " (file name must contain \"" + pattern + "\")."};
        }

        return tables;
    }

    /**
     * Updates the local variable holding the instrument name when the value
     * changes.
     * 
     * @param instrumentName the name of the new instrument.
     */
    private void setDir(String dir) {
    	this.dir = dir;
    }

    private final BaseObserver<String> dirAdapter = new BaseObserver<String>() {
        @Override
        public void onValue(String value) {
            setDir(value);
        }
    };
}
