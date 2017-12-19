
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.blockselector;

import java.util.Collection;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.DisablingConfigHandler;

/**
 * Provides access to the current configuration and displays the block selection dialog.
 * Provides access to the selected block name and the block PV address.
 *
 */
public class BlockSelector extends DisablingConfigHandler<Configuration> {
	
	private String blockName = "";
	private String pvAddress = "";
    private boolean confirmed = false;

    /**
     * The constructor.
     */
	public BlockSelector() {
		super(SERVER.setCurrentConfig());
	}

    /**
     * {@inheritDoc}
     */
	@Override
    public void safeExecute(ExecutionEvent event) {
        Collection<EditableBlock> availableBlocks = EDITING.currentConfig().getValue().getOtherBlocks();
		
        BlockSelectorDialog dialog = new BlockSelectorDialog(null, availableBlocks);
		if (dialog.open() == Window.OK) {
			blockName = dialog.getBlockName();
			pvAddress = dialog.getPVAddress();
            confirmed = true;
		}
	}
	
    /**
     * Returns the block's name.
     * 
     * @return the name
     */
	public String getBlockName() {
		return blockName;
	}
	
    /**
     * Get the associated PV address.
     * 
     * @return the address
     */
	public String getPvAddress() {
		return pvAddress;
	}

    /**
     * Get whether confirmed.
     * 
     * @return true for confirmed
     */
    public boolean isConfirmed() {
        return confirmed;
    }
}
