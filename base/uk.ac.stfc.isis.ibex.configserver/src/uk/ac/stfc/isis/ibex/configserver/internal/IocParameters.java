
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

package uk.ac.stfc.isis.ibex.configserver.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import uk.ac.stfc.isis.ibex.configserver.configuration.AvailablePV;
import uk.ac.stfc.isis.ibex.configserver.configuration.AvailablePVSet;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;

/**
 * NB This class holds data coming from IOCS, so it lists the properties 
 * available to be set on an IOC, rather than those actually set in a config
 */
public class IocParameters {

	private final boolean running;
    private final String description;
	private List<Macro> macros = new ArrayList<Macro>();
	private List<AvailablePV> pvs = new ArrayList<AvailablePV>();
	private List<AvailablePVSet> pvsets = new ArrayList<AvailablePVSet>();
	private final String host;

	/**
	 * Builds a new set of IOC parameters.
	 * @param running - whether the IOC is running.
	 * @param macros - The macros to use.
	 * @param pvs - The pvs to use.
	 * @param pvsets - The pvsets associated with this IOC.
	 * @param description - The description of this IOC.
	 * @param host - The host this IOC is running on.
	 */
    public IocParameters(boolean running, Collection<Macro> macros, Collection<AvailablePV> pvs,
            Collection<AvailablePVSet> pvsets, String description, String host) {
		this.running = running;
        this.description = description;
        this.host = host;

		for (Macro macro : macros) {
			this.macros.add(macro);
		}
		
		for (AvailablePV pv : pvs) {
			this.pvs.add(pv);
		}
		
		for (AvailablePVSet pvset : pvsets) {
			this.pvsets.add(pvset);
		}
	}
	
    /**
     * Whether this ioc is running.
     * @return - true if ioc is running
     */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * The macros that are available to be set of this IOC (not necessarily actually set in a config).
	 * @return the macros.
	 */
	public Collection<Macro> getMacros() {
		return macros;
	}
	
	/**
	 * The pvs available to be set on this IOC.
	 * @return the pvs.
	 */
	public Collection<AvailablePV> getPVs() {
		return pvs;
	}
	
	/**
	 * The PV sets available to this IOC.
	 * @return the pv sets.
	 */
	public Collection<AvailablePVSet> getPVSets() {
		return pvsets;
	}

	/**
	 * The description of this IOC.
	 * @return the description.
	 */
    public String getDescription() {
        return description;
    }
    
    /**
     * The host which this IOC is running on.
     * @return the hostname.
     */
    public String getHost() {
    	return host;
    }
}
