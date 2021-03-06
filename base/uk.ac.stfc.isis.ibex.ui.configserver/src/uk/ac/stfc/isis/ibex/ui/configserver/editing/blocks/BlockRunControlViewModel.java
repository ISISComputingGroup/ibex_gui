
/**
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;

/**
 * The view model for the run-control settings for a block.
 */
public class BlockRunControlViewModel extends AbstractRunControlViewModel {    
    
    private final Block editingBlock;
	
    /**
     * Constructor.
     * 
     * @param editingBlock the block being edited
     */
    public BlockRunControlViewModel(final Block editingBlock) {
    	this.editingBlock = editingBlock;
    	resetFromSource();
	}
    
    /**
     * Update the stored settings.
     */
    public void updateBlock() {
    	editingBlock.setRunControlHighLimit(getRunControlHighLimit());
    	editingBlock.setRunControlLowLimit(getRunControlLowLimit());
    	editingBlock.setRunControlEnabled(getRunControlEnabled());
    	editingBlock.setSuspendIfInvalid(getSuspendIfInvalid());
    }

    /**
     * {@inheritDoc}
     */
	@Override
	public void resetFromSource() {
		setRunControlLowLimit(editingBlock.getRunControlLowLimit());
    	setRunControlHighLimit(editingBlock.getRunControlHighLimit());
    	setRunControlEnabled(editingBlock.getRunControlEnabled());
    	setSuspendIfInvalid(editingBlock.getSuspendIfInvalid());
	}
}
