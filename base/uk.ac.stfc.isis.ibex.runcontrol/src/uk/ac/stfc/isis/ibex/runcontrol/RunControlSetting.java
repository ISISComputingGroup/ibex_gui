
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

package uk.ac.stfc.isis.ibex.runcontrol;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class RunControlSetting extends ModelObject {

	private String blockName;
	private String lowLimit;
	private String highLimit;
	private boolean enabled;
	
	public RunControlSetting(String blockName, String lowLimit, String highLimit, boolean enabled) {
			this.blockName = blockName;
			this.lowLimit = lowLimit;
			this.highLimit = highLimit;
			this.enabled = enabled;
	}
	
	public RunControlSetting(RunControlSetting other) {
		this(other.blockName, other.lowLimit, other.highLimit, other.enabled);
	}
	
	public String getBlockName() {
		return blockName;
	}
	
	public String getLowLimit() {
		return lowLimit;
	}

	public void setLowLimit(String limit) {
		firePropertyChange("lowlimit", this.lowLimit, this.lowLimit = limit);
	}
	
	public String getHighLimit() {
		return highLimit;
	}

	public void setHighLimit(String limit) {
		firePropertyChange("highlimit", this.highLimit, this.highLimit = limit);
	}
	
	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		firePropertyChange("enabled", this.enabled, this.enabled = enabled);
	}

}
