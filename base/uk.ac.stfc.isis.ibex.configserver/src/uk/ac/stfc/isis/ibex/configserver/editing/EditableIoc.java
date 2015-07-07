
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

public class EditableIoc extends Ioc {

	private Collection<Macro> availableMacros = new ArrayList<Macro>();
	private Collection<AvailablePVSet> availablePVSets = new ArrayList<AvailablePVSet>();
	private Collection<AvailablePV> availablePVs = new ArrayList<AvailablePV>();

	public EditableIoc(String name) {
		super(name);
	}
	
	public EditableIoc(Ioc other) {
		super(other);
	}
	
	public EditableIoc(EditableIoc other) {
		this((Ioc)other);
		
		for (Macro macro : other.getAvailableMacros()) {
			availableMacros.add(new Macro(macro));
		}
		
		for (AvailablePVSet pvset : other.getAvailablePVSets()) {
			availablePVSets.add(pvset);
		}
		
		for (AvailablePV pv : other.getAvailablePVs()) {
			availablePVs.add(pv);
		}
	}

	public Collection<Macro> getAvailableMacros() {
		if (availableMacros == null) {
			availableMacros = new ArrayList<>();
		}
		
		return availableMacros;
	}
	
	public void setAvailableMacros(Collection<Macro> macros) {
		firePropertyChange("availableMacros", this.availableMacros, this.availableMacros = macros);
	}

	public Collection<AvailablePVSet> getAvailablePVSets() {
		if (availablePVSets == null) {
			availablePVSets = new ArrayList<>();
		}
		
		return availablePVSets;
	}
	
	public void setAvailablePVSets(Collection<AvailablePVSet> availablePVSets) {
		firePropertyChange("availablePVSets", this.availablePVSets, this.availablePVSets = availablePVSets);
	}
	
	public String getDescription() {
		return "";
	}
	
	public boolean isEditable() {
		return !hasSubConfig();
	}
	
	// Find a actual (rather than available) PVSet and return it, or null
	public PVSet findPVSet(String name) {
		for (PVSet pvset : getPvSets()) {
			if (pvset.getName().equals(name)) {
				return pvset;
			}
		}
		return null;
	}
	
	public Collection<AvailablePV> getAvailablePVs() {
		if (availablePVs == null) {
			availablePVs = new ArrayList<>();
		}
		
		return availablePVs;
	}
	public void setAvailablePVs(Collection<AvailablePV> pvs) {
		firePropertyChange("availablePVs", this.availablePVs, this.availablePVs = pvs);
	}
}
