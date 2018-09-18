
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

package uk.ac.stfc.isis.ibex.configserver.editing;

import uk.ac.stfc.isis.ibex.configserver.configuration.PVSet;

/**
 *  Extends a PV set to make it easier to edit.
 *
 */
public class EditablePVSet extends PVSet {
	private String description;
	
	/**
	 * The constructor for a class that makes a PV set easier to edit.
	 * @param other
	 *             The PV set to edit.
	 * @param description
	 *             The PV set's description.
	 */
	public EditablePVSet(PVSet other, String description) {
		super(other);
		this.description = description;
	}
	
	/**
     * The constructor for a class that makes a PV set easier to edit.
     * @param name
     *             The editable PV set's name.
     * @param editable
     *              True if the PV set is editable.
     * @param description
     *             The editable PV set's description.
     */
	public EditablePVSet(String name, boolean editable, String description) {
		super(name, editable);
		this.description = description;
	}
	
	/**
	 * Returns the PV set's description.
	 * @return
	 *         The PV set's description.
	 */
	public String getDescription() {
		return description;
	}
}
