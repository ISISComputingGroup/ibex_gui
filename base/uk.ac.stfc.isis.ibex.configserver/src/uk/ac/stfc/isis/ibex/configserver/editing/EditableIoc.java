
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

package uk.ac.stfc.isis.ibex.configserver.editing;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.configuration.AvailablePV;
import uk.ac.stfc.isis.ibex.configserver.configuration.AvailablePVSet;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.configuration.PVSet;

/**
 * Holds an Editable IOC, which is used by the configuration editor.
 * 
 * Also holds the descriptor for the IOC, describing what the IOC is physically used for.
 *
 */
public class EditableIoc extends Ioc {

	private Collection<Macro> availableMacros = new ArrayList<Macro>();
	private Collection<AvailablePVSet> availablePVSets = new ArrayList<AvailablePVSet>();
	private Collection<AvailablePV> availablePVs = new ArrayList<AvailablePV>();
    private String description = "";

    /**
     * An IOC which can be edited as part of a configuration.
     * 
     * @param name
     *            The IOC name
     */
	public EditableIoc(String name) {
		super(name);
	}
	
    /**
     * Create an editable IOC with properties matching another IOC.
     * 
     * @param description
     *            A description of the IOC
     * @param other
     *            The IOC whose properties to match
     */
    public EditableIoc(Ioc other, String description) {
        super(other);
        this.description = description;
	}
	
    /**
     * Create an editable IOC with properties matching another editable IOC.
     * 
     * @param other
     *            The Editable IOC whose properties to match
     */
	public EditableIoc(EditableIoc other) {
        this(other, other.description);
		
		for (Macro macro : other.getAvailableMacros()) {
			availableMacros.add(new Macro(macro));
		}
		
		for (AvailablePVSet pvset : other.getAvailablePVSets()) {
			availablePVSets.add(pvset);
		}
		
		for (AvailablePV pv : other.getAvailablePVs()) {
			availablePVs.add(pv);
		}
        this.description = other.getDescription();
	}

    /**
     * @return The macros available to edit
     */
	public Collection<Macro> getAvailableMacros() {
		if (availableMacros == null) {
			availableMacros = new ArrayList<>();
		}
		
		return availableMacros;
	}
	
    /**
     * @param macros
     *            The new macros to apply to the IOC
     */
	public void setAvailableMacros(Collection<Macro> macros) {
		firePropertyChange("availableMacros", this.availableMacros, this.availableMacros = macros);
	}

    /**
     * @return The PV sets available to edit
     */
	public Collection<AvailablePVSet> getAvailablePVSets() {
		if (availablePVSets == null) {
			availablePVSets = new ArrayList<>();
		}
		
		return availablePVSets;
	}
	
    /**
     * @param availablePVSets
     *            The new PV set values
     */
	public void setAvailablePVSets(Collection<AvailablePVSet> availablePVSets) {
		firePropertyChange("availablePVSets", this.availablePVSets, this.availablePVSets = availablePVSets);
	}
	
    /**
     * @param description
     *            The new description
     */
    public void setDescription(String description) {
        firePropertyChange("description", this.description, this.description = description);
    }

    /**
     * @return The current IOC description
     */
	public String getDescription() {
        return this.description;
	}
	
    /**
     * @return Whether the IOC is editable
     */
	public boolean isEditable() {
		return !hasComponent();
	}
	
    /**
     * Find a actual (rather than available) PVSet and return it, or null.
     * 
     * @param name
     *            The name of the set
     * @return The PV set
     */
	public PVSet findPVSet(String name) {
		for (PVSet pvset : getPvSets()) {
			if (pvset.getName().equals(name)) {
				return pvset;
			}
		}
		return null;
	}
	
    /**
     * @return A collection of available PVs
     */
	public Collection<AvailablePV> getAvailablePVs() {
		if (availablePVs == null) {
			availablePVs = new ArrayList<>();
		}
		
		return availablePVs;
	}
	
    /**
     * @param pvs
     *            The new collection of available PVs
     */
	public void setAvailablePVs(Collection<AvailablePV> pvs) {
		firePropertyChange("availablePVs", this.availablePVs, this.availablePVs = pvs);
	}
}
