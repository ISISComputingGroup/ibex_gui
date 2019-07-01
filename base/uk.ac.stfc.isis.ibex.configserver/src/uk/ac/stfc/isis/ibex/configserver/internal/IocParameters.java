
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
 * available to be set on an IOC, rather than those actually set in a config.
 * 
 * This class is constructed from JSON, so the constructor may never explicitly be
 * called from our code (but it will be called by the GSON library).
 */
public class IocParameters {

	private final boolean running;
    private final String description;
	private final List<Macro> macros;
	private final List<AvailablePV> pvs;
	private final List<AvailablePVSet> pvsets;
	private final String remotePvPrefix;

	/**
	 * Builds a new set of IOC parameters.
	 * @param running - whether the IOC is running.
	 * @param macros - The macros to use.
	 * @param pvs - The pvs to use.
	 * @param pvsets - The pvsets associated with this IOC.
	 * @param description - The description of this IOC.
	 * @param remotePvPrefix - The pv prefix of the host this IOC is running on.
	 */
    public IocParameters(boolean running, Collection<Macro> macros, Collection<AvailablePV> pvs,
            Collection<AvailablePVSet> pvsets, String description, String remotePvPrefix) {
		this.running = running;
        this.description = description;
        this.remotePvPrefix = remotePvPrefix;
		
		this.macros = new ArrayList<>(macros);
		this.pvs = new ArrayList<>(pvs);
		this.pvsets = new ArrayList<>(pvsets);
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
     * The pv prefix of the host which this IOC is running on.
     * @return the remote pv prefix.
     */
    public String getRemotePvPrefix() {
    	return remotePvPrefix;
    }
}
