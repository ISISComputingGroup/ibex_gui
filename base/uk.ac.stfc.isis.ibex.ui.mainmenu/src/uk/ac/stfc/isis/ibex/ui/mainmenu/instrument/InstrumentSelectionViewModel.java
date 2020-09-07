
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

package uk.ac.stfc.isis.ibex.ui.mainmenu.instrument;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;
import uk.ac.stfc.isis.ibex.validators.InstrumentNameValidator;

/**
 * A view model allowing selection of an instrument from a list of instruments,
 * given the instrument name.
 * 
 */
public class InstrumentSelectionViewModel extends ErrorMessageProvider {

    private Collection<InstrumentInfo> instruments;
    private String selectedName = "";

    /**
     * @param instruments a Collection containing the InstrumentInfo.
     */
    public InstrumentSelectionViewModel(Collection<InstrumentInfo> instruments) {
        checkPreconditions(instruments);
        this.instruments = instruments;
    }

    /**
     * @return the name of the currently selected instrument.
     */
    public String getSelectedName() {
        return selectedName;
    }

    /**
     * Validates and sets the name of the selected instrument (Used for testing).
     * 
     * @param name the instrument name to set.
     */
    public void setSelectedName(String name) {
        validate(name);
        firePropertyChange("selectedName", this.selectedName, this.selectedName = name);
    }

    /**
     * @return a collection containing all of the InstrumentInfo
     */
    public Collection<InstrumentInfo> getInstruments() {
        return instruments;
    }

    /**
     * @return the InstrumentInfo for the selected instrument.
     */
    public InstrumentInfo getSelectedInstrument() {
        for (InstrumentInfo instrument : instruments) {
            if (nameMatches(getSelectedName(), instrument.name())) {
                return instrument;
            }
        }

        return null;
    }

    /**
     * @return whether or not the currently selected instrument exists in the list of instruments.
     */
    public boolean selectedInstrumentExists() {
        if (getSelectedName().isEmpty()) {
            return false;
        }

        for (InstrumentInfo instrument : instruments) {
            if (nameMatches(getSelectedName(), instrument.name())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Validate the instrument name against the requirements and set an error.
     */
    public void validate() {
        validate(selectedName);
    }

    private boolean nameMatches(String name1, String name2) {
        return name1.equalsIgnoreCase(name2);
    }

    private void validate(String instrumentName) {
        Collection<String> knownInstrumentNames = extractInstrumentNames();

        InstrumentNameValidator nameValidator = new InstrumentNameValidator(knownInstrumentNames);
        if (!nameValidator.validateInstrumentName(instrumentName)) {
            setError(true, nameValidator.getErrorMessage());
        } else {
            setError(false, null);
        }
    }

    private Collection<String> extractInstrumentNames() {
        ArrayList<String> names = new ArrayList<>();
        for (InstrumentInfo instrument : instruments) {
            names.add(instrument.name());
        }

        return names;
    }

    private void checkPreconditions(Collection<InstrumentInfo> instruments) {
        if (instruments == null) {
            throw new IllegalArgumentException("Input instruments cannot be null");
        }
    }

}
