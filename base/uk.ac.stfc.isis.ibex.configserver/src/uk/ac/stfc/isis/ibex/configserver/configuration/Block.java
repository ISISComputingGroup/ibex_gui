
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

package uk.ac.stfc.isis.ibex.configserver.configuration;

import uk.ac.stfc.isis.ibex.model.ModelObject;

import com.google.common.base.Strings;

public class Block extends ModelObject {

	private String name;
	private String pv;
	private boolean visible;
	private boolean local;
	private String subconfig;
		
	public Block(String name, String pv, boolean visible, boolean local, String subconfig) {
		this.name = name;
		this.pv = pv;
		this.visible = visible;
		this.local = local;
		this.subconfig = subconfig;
	}
	
	public Block(Block other) {
		this(other.name, other.pv, other.visible, other.local, other.subconfig);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}
	
	public String getPV() {
		return pv;
	}

	public void setPV(String pv) {
		firePropertyChange("PV", this.pv, this.pv = pv);
	}
	
	public boolean getIsVisible() {
		return visible;
	}

	public void setIsVisible(boolean isVisible) {
		firePropertyChange("isVisible", this.visible, this.visible = isVisible);
	}
	
	public boolean getIsLocal() {
		return local;
	}
	
	public void setIsLocal(boolean isLocal) {
		firePropertyChange("isLocal", this.local, this.local = isLocal);
	}
	
	public String subconfig() {
		return subconfig;
	}

	public boolean hasSubConfig() {
		return !Strings.isNullOrEmpty(subconfig);
	}
	
	@Override
	public String toString() {
		return name;
	}
}
