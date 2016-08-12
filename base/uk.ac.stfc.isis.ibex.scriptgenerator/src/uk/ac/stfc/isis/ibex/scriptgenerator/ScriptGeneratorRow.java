
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

package uk.ac.stfc.isis.ibex.scriptgenerator;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class ScriptGeneratorRow extends ModelObject {
	private String name;
	private int temperature;
	private boolean wait;
	
	public ScriptGeneratorRow(String name, int temperature, boolean wait) {
		this.name = name;
		this.temperature = temperature;
		this.wait = wait;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}
	
	public int getTemperature() {
		return this.temperature;
	}
	
	public void setTemperature(int temperature) {
		firePropertyChange("temperature", this.temperature, this.temperature = temperature);
	}
	
	public boolean getWait() {
		return this.wait;
	}
	
	public void setWait(boolean wait) {
		firePropertyChange("wait", this.wait, this.wait = wait);
	}
	
	public String toString() {
		return name;
	}
}
