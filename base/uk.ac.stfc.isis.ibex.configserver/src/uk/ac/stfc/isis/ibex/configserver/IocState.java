
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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.configserver;

import uk.ac.stfc.isis.ibex.epics.observing.INamed;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Class to hold information about the state of an IOC.
 *
 */
public class IocState extends ModelObject implements Comparable<IocState>, INamed {
	private final String name;
	private final boolean allowControl;

	private boolean isRunning;
	private String description;
	
    /**
     * Instantiates a new IOC state.
     *
     * @param name
     *            the name
     * @param isRunning
     *            whether the IOC is running
     * @param description
     *            description of the IOC
     * @param allowControl
     *            whether the user is allowed control it
     */
	public IocState(String name, boolean isRunning, String description, boolean allowControl) {
		this.name = name;
		this.isRunning = isRunning;
		this.description = description;
		this.allowControl = allowControl;
	}
	
    /**
     * Copy constructor.
     *
     * @param other
     *            the value to copy
     */
	public IocState(IocState other) {
		this(other.name, other.isRunning, other.description, other.allowControl);
	}	

	@Override
    public String getName() {
		return name;
	}
	
    /**
     * 
     * @return True if it is running; False otherwise
     */
	public boolean getIsRunning() {
		return isRunning;
	}
	
    /**
     *
     * @param isRunning
     *            true if it is running; False if not running
     */
	public void setIsRunning(boolean isRunning) {
		firePropertyChange("isRunning", this.isRunning, this.isRunning = isRunning);
	}
	
    /**
     * Gets the description.
     *
     * @return the description
     */
	public String getDescription() {
		return description;
	}
	
    /**
     *
     * @return true if user is allowed to control it; false otherwise
     */
	public boolean getAllowControl() {
		return allowControl;
	}

	@Override
	public int compareTo(IocState iocState) {
		return name.compareTo(iocState.getName());
	}
}
