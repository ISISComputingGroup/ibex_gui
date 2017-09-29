
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

package uk.ac.stfc.isis.ibex.ui.synoptic.editor.pv;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.model.Awaited;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.configserver.commands.DisablingConfigHandler;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.PvSelectorDialog;

/**
 * Provides access to the current configuration and displays the PV selection
 * dialog Provides access to the selected PV. *
 */
public class PvSelector extends DisablingConfigHandler<Configuration> {
	
	private String pvAddress = "";
    private boolean confirmed = false;

    /**
     * The constructor.
     */
	public PvSelector() {
		super(SERVER.setCurrentConfig());
	}

    /**
     * {@inheritDoc}
     */
	@Override
    public void safeExecute(ExecutionEvent event) {
		UpdatedValue<EditableConfiguration> config = new UpdatedObservableAdapter<>(EDITING.currentConfig());
		
		if (Awaited.returnedValue(config, 1)) {
			openDialog(config.getValue());
		}
	}
	
    /**
     * Opens the dialog.
     * 
     * @param config
     *            the configuration
     */
	private void openDialog(EditableConfiguration config) {
		PvSelectorDialog dialog = new PvSelectorDialog(null, config, "");	
		if (dialog.open() == Window.OK) {
			pvAddress = dialog.getPVAddress();
            confirmed = true;
		}
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
