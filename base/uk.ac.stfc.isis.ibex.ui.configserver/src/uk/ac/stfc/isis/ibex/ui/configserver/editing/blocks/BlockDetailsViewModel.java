
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

import org.eclipse.jface.window.Window;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.editing.BlockNameValidator;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.PvSelectorDialog;
import uk.ac.stfc.isis.ibex.validators.PvValidator;

public class BlockDetailsViewModel extends ErrorMessageProvider {

	private String name = "";
	private String pvAddress = "";
	private boolean visible = false;
	private boolean local = false;
	private boolean enabled = true;
    
    private final Block editingBlock;
    private final EditableConfiguration config;
    
    public BlockDetailsViewModel(final EditableBlock editingBlock, EditableConfiguration config) {
    	this.editingBlock = editingBlock;
    	this.config = config;
    	
    	if (editingBlock != null) {
	    	name = editingBlock.getName();
	    	pvAddress = editingBlock.getPV();
	    	visible = editingBlock.getIsVisible();
	    	local = editingBlock.getIsLocal();
	    	enabled = editingBlock.isEditable();
    	}

	}

	public void setEnabled(boolean enabled) {
		firePropertyChange("enabled", this.enabled, this.enabled = enabled);
	}
	
	public boolean getEnabled() {
		return enabled;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (validate(name, pvAddress))
				firePropertyChange("name", this.name, this.name = name);
	}

	public String getPvAddress() {
		return pvAddress;
	}

	public void setPvAddress(String pvAddress) {
		if (validate(name, pvAddress))
			firePropertyChange("pvAddress", this.pvAddress, this.pvAddress = pvAddress);
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		firePropertyChange("visible", this.visible, this.visible = visible);
	}

	public boolean isLocal() {
		return local;
	}

	public void setLocal(boolean local) {
		firePropertyChange("local", this.local, this.local = local);
	}
    
    public void openPvDialog() {
		PvSelectorDialog pvDialog = new PvSelectorDialog(null, config, pvAddress);	
		if (pvDialog.open() == Window.OK) {
			setPvAddress(pvDialog.getPVAddress());
		}
    }
	
    public void updateBlock() {
    	editingBlock.setName(name);
    	editingBlock.setPV(pvAddress);
    	editingBlock.setIsLocal(local);
    	editingBlock.setIsVisible(visible);
    }
    
    private boolean validate(String name, String pvAddress) {
    	BlockNameValidator nameVal = new BlockNameValidator(config, editingBlock);
        PvValidator addressValid = new PvValidator();
    	boolean is_valid = false;
    	
    	if (!(nameVal.isValidName(name))) {
    		setError(true, nameVal.getErrorMessage());
    	} else if (!(addressValid.validatePvAddress(pvAddress))) {
    		setError(true, addressValid.getErrorMessage());
    	} else {
    		is_valid = true;
    		setError(false, null);
    	}
    	return is_valid;
    }
}
