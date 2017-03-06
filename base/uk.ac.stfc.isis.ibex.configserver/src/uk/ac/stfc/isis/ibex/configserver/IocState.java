
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

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Class to hold information about an IOC.
 *
 */
public class IocState extends ModelObject implements Comparable<IocState> {
	private final String name;
	private final boolean allowControl;

	private boolean isRunning;
	private String description;
	
	public IocState(String name, boolean isRunning, String description, boolean allowControl) {
		this.name = name;
		this.isRunning = isRunning;
		this.description = description;
		this.allowControl = allowControl;
	}
	
	public IocState(IocState other) {
		this(other.name, other.isRunning, other.description, other.allowControl);
	}	

	public String getName() {
		return name;
	}
	
	public boolean getIsRunning() {
		return isRunning;
	}
	
	public void setIsRunning(boolean isRunning) {
		firePropertyChange("isRunning", this.isRunning, this.isRunning = isRunning);
	}
	
	public String getDescription() {
		return description;
	}
	
	public boolean getAllowControl() {
		return allowControl;
	}

	@Override
	public int compareTo(IocState arg0) {
		return name.compareTo(arg0.getName());
	}
}
