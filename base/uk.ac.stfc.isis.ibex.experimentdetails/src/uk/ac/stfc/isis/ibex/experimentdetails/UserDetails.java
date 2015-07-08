
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

package uk.ac.stfc.isis.ibex.experimentdetails;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class UserDetails extends ModelObject {
	
	private String name;
	private String institute;
	private Role role;

	public UserDetails(String name, String institute, Role role) {
		this.name = name;
		this.institute = institute;
		this.role = role;
	}
	
	public UserDetails(UserDetails other) {
		this(other.name, other.institute, other.role);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		firePropertyChange("institute", this.institute, this.institute = institute);
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		firePropertyChange("role", this.role, this.role = role);
	}

}
