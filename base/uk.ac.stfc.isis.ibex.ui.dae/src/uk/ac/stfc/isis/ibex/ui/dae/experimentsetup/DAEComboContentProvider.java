
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

    /**
     * Constructor. Binds the name of the current instrument to display in
     * messages.
     * 
     * @param instrument The instrument name.
     */
    public DAEComboContentProvider(ForwardingObservable<String> instrument) {
        this.instrumentName = instrument.getValue();
        instrument.addObserver(instrumentAdapter);
    }

    /**
     * Returns a string array from a string collection, or an empty array if the
     * input is null.
     * 
     * @param updated the string collection.
     * @return the resulting array.
     */
    private String[] valueOrEmpty(UpdatedValue<Collection<String>> updated) {
        Collection<String> value = updated.getValue();
        return value != null ? value.toArray(new String[0]) : new String[0];
    }

    /**
     * Adds a blank option to the list for displaying in a drop down menu in the
     * GUI.
     * 
     * @param files a list of available files.
     * @return the list of files with a blank entry added at the beginning.
     */
    private String[] addBlank(String[] tables) {
        String[] result = new String[tables.length + 1];
        result[0] = " ";
        for (int i = 0; i < tables.length; i++) {
            result[i + 1] = tables[i];
        }
        return result;
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
            tables = new String[] {"Error retrieving information from Instrument." };
        } else if (tables.length == 0) {
            String instrument;
            if (this.instrumentName == null) {
                instrument = "<instrument>";
            } else {
                instrument = this.instrumentName;
            }
            tables = new String[] { "None found in C:\\Instrument\\Settings\\config\\" + instrument
                    + "\\configurations\\tables\\ (file name must contain \"" + pattern + "\")." };
        }

        return addBlank(tables);
    }

    /**
     * Updates the local variable holding the instrument name when the value
     * changes.
     * 
     * @param instrumentName the name of the new instrument.
     */
    private void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }

    private final BaseObserver<String> instrumentAdapter = new BaseObserver<String>() {
        @Override
        public void onValue(String value) {
            setInstrumentName(value);
        }
    };
}
