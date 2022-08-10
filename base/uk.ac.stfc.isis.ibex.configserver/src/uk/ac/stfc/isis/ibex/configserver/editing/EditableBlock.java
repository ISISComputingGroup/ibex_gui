
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

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;

/**
 * A block that may be editable.
 */
public class EditableBlock extends Block {

	/**
	 * Create a block that may be editable from a read only one.
	 * @param other The original block.
	 */
	public EditableBlock(Block other) {
		super(other);
	}
	
	/**
	 * Create a block that may be editable from a read-only one which is part of a component.
	 * @param other The original block.
	 * @param componentName The name of the component that this block is part of.
	 */
	public EditableBlock(Block other, String componentName) {
		super(other);
		this.component = componentName;
	}

	/**
	 * Get whether the block is editable.
	 * @return True if the block can be edited.
	 */
	public boolean isEditable() {
		return !inComponent();
	}
}
