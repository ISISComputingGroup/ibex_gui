
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

package uk.ac.stfc.isis.ibex.synoptic.model;

import java.util.List;
import java.util.Set;

import uk.ac.stfc.isis.ibex.devicescreens.components.ComponentType;
import uk.ac.stfc.isis.ibex.targets.Target;

/**
 * An interface representing a component in the synoptic.
 */
public interface Component {
    
    /**
     * @return the name of the component
     */
	String name();

	/**
	 * @return the type of the component
	 */
	ComponentType type();
	
	/**
	 * @return a set containing the component's properties
	 */
	Set<ComponentProperty> properties();
	
	List<? extends Component> components();
	
	Target target();
	
	void setTarget(Target target);
	
	/**
	 * Creates a copy of the component.
	 * 
	 * @return a copy of the component
	 */
	Component copy();
}
