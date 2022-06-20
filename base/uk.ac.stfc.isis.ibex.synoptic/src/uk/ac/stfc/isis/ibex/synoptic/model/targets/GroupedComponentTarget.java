
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

package uk.ac.stfc.isis.ibex.synoptic.model.targets;

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.targets.Target;

/**
 * Contains a group components to display.
 */
public class GroupedComponentTarget extends Target {

	private final List<Component> components;
	
	/**
	 * Create the group of components.
	 * 
	 * @param name The name of the target.
	 * @param components The components in the target.
	 */
	public GroupedComponentTarget(String name, List<? extends Component> components) {
		super(name);
		this.components = new ArrayList<>(components);
	}
	
	/**
	 * @return The components that are grouped together.
	 */
	public List<Component> components() {
		return components;
	}	
}
