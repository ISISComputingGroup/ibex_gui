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

package uk.ac.stfc.isis.ibex.synoptic.model.desc;

import java.util.List;

/**
 * Interface to allow for general access to component parents.
 */
public interface SynopticParentDescription {
	
	/**
	 * @return The components of this parent.
	 */
	List<ComponentDescription> components();
	
	/**
	 * Add the given component to this parent.
	 * 
	 * @param component The component to add.
	 */
	void addComponent(ComponentDescription component);
	
	/**
	 * Add the given component to this parent at the given index.
	 * 
	 * @param component The component to add.
	 * @param index The index to add it at.
	 */
	void addComponent(ComponentDescription component, int index);
	
	/**
	 * Remove the given component from this parent.
	 * 
	 * @param component The component to remove.
	 */
	void removeComponent(ComponentDescription component);
}
