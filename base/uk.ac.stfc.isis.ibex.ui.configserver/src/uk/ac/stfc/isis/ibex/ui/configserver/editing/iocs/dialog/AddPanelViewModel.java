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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * A view model storing the information needed to add an IOc to a configuration.
 */
public class AddPanelViewModel extends ModelObject {

    private String selectedName;
    private String currentSelection;
    private Hashtable<String, ArrayList<EditableIoc>> availableIocs;

    /**
     * Constructor for the add panel view model.
     * 
     * @param availableIocs
     *            The IOCs available to choose from.
     */
    public AddPanelViewModel(Collection<EditableIoc> availableIocs) {
    	this.availableIocs = new Hashtable<String, ArrayList<EditableIoc>>();
    	String description = "";
    	for (EditableIoc ioc : availableIocs) {
    		description = ioc.getDescription();
    		if (!this.availableIocs.containsKey(description)) {
    			this.availableIocs.put(description, new ArrayList<EditableIoc>());
    		}
    		this.availableIocs.get(description).add(ioc);
    	}
    }

    /**
     * Get the available IOCs.
     * 
     * @return The available IOCs.
     */
    public Hashtable<String, ArrayList<EditableIoc>> getAvailableIocs() {
        return availableIocs;
    }

    /**
     * Get the IOC that has been selected.
     * 
     * @return The selected IOC
     */
    public TempEditableIoc getSelectedIoc() {
        for (EditableIoc ioc : availableIocs.get(currentSelection)) {
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

    /**
     * Gets the IOCs description (the key to Hashtable).
     * @return The selected Ioc's description
     */
	public String getCurrentSelection() {
		return currentSelection;
	}

	/**
     * Sets the IOCs description (the key to Hashtable).
     * 
     * @param currentSelection
     *            The IOCs description
     */
	public void setCurrentSelection(String currentSelection) {
		this.currentSelection = currentSelection;
	}

}
