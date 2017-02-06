
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

// Class to provide the lists of selected and unselected Components for editing
package uk.ac.stfc.isis.ibex.configserver.editing;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.model.ExclusiveSetPair;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Class holding information of selected and unselected editable components as
 * part of a configuration.
 */
public class EditableComponents extends ModelObject {
	
    private final ExclusiveSetPair<Configuration> components;

    /**
     * Constructor.
     * 
     * @param selected
     *            The components selected to be part of this configuration.
     * @param unselected
     *            The unselected components available to be added to the
     *            configuration.
     */
    public EditableComponents(Collection<Configuration> selected, Collection<Configuration> unselected) {
        components = new ExclusiveSetPair<Configuration>(unselected, selected);
	}
	
    /**
     * @return The unselected components
     */
    public Collection<Configuration> getUnselected() {
		return new ArrayList<>(components.unselected());
	}
	
    /**
     * @return The selected components
     */
    public Collection<Configuration> getSelected() {
		return new ArrayList<>(components.selected());
	}
	
    /**
     * Toggles the specified components from unselected to selected or vice
     * versa.
     * 
     * @param componentsToToggle
     *            The components to be toggled
     */
    public synchronized void toggleSelection(Collection<Configuration> componentsToToggle) {
        Collection<Configuration> selectedBefore = getSelected();
        Collection<Configuration> unselectedBefore = getUnselected();
		
        for (Configuration component : componentsToToggle) {
			components.move(component);
		}
		
		firePropertyChange("selected", selectedBefore, getSelected());
		firePropertyChange("unselected", unselectedBefore, getUnselected());
	}
}
