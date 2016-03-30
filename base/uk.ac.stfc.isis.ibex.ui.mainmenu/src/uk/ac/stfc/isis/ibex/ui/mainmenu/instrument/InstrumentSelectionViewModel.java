
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

import java.util.Collection;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class InstrumentSelectionViewModel extends ModelObject {

    private Collection<InstrumentInfo> instruments;
    private String selectedName = "";

    public InstrumentSelectionViewModel(Collection<InstrumentInfo> instruments) {
        this.instruments = instruments;
    }

    public String getSelectedName() {
        return selectedName;
    }

    public void setSelectedName(String name) {
        firePropertyChange("selectedName", this.selectedName, this.selectedName = name);
    }

    public Collection<InstrumentInfo> getInstruments() {
        return instruments;
    }

    public InstrumentInfo getSelectedInstrument() {
        for (InstrumentInfo instrument : instruments) {
            if (nameMatches(getSelectedName(), instrument.name())) {
                return instrument;
            }
        }

        return null;
    }

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

    private boolean nameMatches(String name1, String name2) {
        return name1.equalsIgnoreCase(name2);
    }
}
