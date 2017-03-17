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
package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.dialog;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * A view model storing the information needed to add an IOc to a configuration.
 */
public class AddPanelViewModel extends ModelObject {

    private String selectedName;
    private Collection<EditableIoc> availableIocs;

    /**
     * Constructor for the add panel view model.
     * 
     * @param availableIocs
     *            The IOCs available to choose from.
     */
    public AddPanelViewModel(Collection<EditableIoc> availableIocs) {
        this.availableIocs = availableIocs;
    }

    /**
     * Get the available IOCs.
     * 
     * @return The available IOCs.
     */
    public Collection<EditableIoc> getAvailableIocs() {
        return availableIocs;
    }

    /**
     * Get the IOC that has been selected.
     * 
     * @return The selected IOC
     */
    public TempEditableIoc getSelectedIoc() {
        for (EditableIoc ioc : availableIocs) {
            if (ioc.getName().equals(selectedName)) {
                return new TempEditableIoc(ioc);
            }
        }
        return new TempEditableIoc(new EditableIoc(selectedName));
    }

    /**
     * @return The IOC name
     */
    public String getSelectedName() {
        return selectedName;
    }

    /**
     * Lets other models know when the IOC has been confirmed (such as through
     * double click).
     */
    public void iocConfirmed() {
        firePropertyChange("confirmed", false, true);
    }

    /**
     * Sets the IOC name.
     * 
     * @param selectedName
     *            The IOC name
     */
    public void setSelectedName(String selectedName) {
        firePropertyChange("selectedName", this.selectedName, this.selectedName = selectedName);
    }

}
