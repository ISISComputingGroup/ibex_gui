
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2019 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.synoptic.model;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public abstract class ComponentProperty extends ModelObject {
	
	private final String displayName;
	
	
	public ComponentProperty(String displayName) {
		this.displayName = displayName;
	}

	public String displayName() {
		return displayName;
	}
	
//	@Override
//	public boolean equals(Object obj) {
//		if (!(obj instanceof ComponentProperty)) {
//			return false;
//		}
//		
//		if (obj == this) {
//			return true;
//		}
//		
//		ComponentProperty other = (ComponentProperty) obj;
//		return displayName.equals(other.displayName);
//	}
//
//	@Override
//	public int hashCode() {
//		return displayName.hashCode();
//	}
}
