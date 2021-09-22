
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
import java.util.Arrays;
import java.util.List;

import uk.ac.stfc.isis.ibex.configserver.BlockRules;
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.editing.BlockNameValidator;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.PvSelectorDialog;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;
import uk.ac.stfc.isis.ibex.validators.PvValidator;

/**
 * Provides information relating to a block.
 */
public class BlockDetailsViewModel extends ErrorMessageProvider {

	private String name = "";
	private String pvAddress = "";
	private boolean visible = false;
	private boolean local = false;
	private boolean enabled = true;
    
    private final Block editingBlock;
    private final EditableConfiguration config;
    
    private final List<String> facilityPvList;

    /**
     * Constructor.
     * 
     * @param editingBlock the block to edit
     * @param config the configuration the block belongs to
     */
    public BlockDetailsViewModel(final EditableBlock editingBlock, EditableConfiguration config) {
    	this.editingBlock = editingBlock;
    	this.config = config;
        this.facilityPvList = getFacilityPVs();
    	
    	if (editingBlock != null) {
	    	name = editingBlock.getName();
	    	pvAddress = editingBlock.getPV();
	    	visible = editingBlock.getIsVisible();
	    	local = editingBlock.getIsLocal();
	    	enabled = editingBlock.isEditable();
    	}

	}

    /**
     * Enable or disable the block.
     * 
     * @param enabled true for enabled
     */
	public void setEnabled(boolean enabled) {
		firePropertyChange("enabled", this.enabled, this.enabled = enabled);
	}
	
    /**
     * @return true if the block is enabled
     */
	public boolean getEnabled() {
		return enabled;
	}
	
    /**
     * @return the block's name
     */
	public String getName() {
		return name;
	}

    /**
     * Set the block's name.
     * 
     * @param name the new name
     */
	public void setName(String name) {
		validate(name, pvAddress);
		firePropertyChange("name", this.name, this.name = name);
	}

    /**
     * @return the PV address
     */
	public String getPvAddress() {
		return pvAddress;
	}

    /**
     * Set the block's PV address.
     * 
     * @param pvAddress the new address
     */
	public void setPvAddress(String pvAddress) {
		validate(name, pvAddress);
        checkFacilityPv(pvAddress);
		firePropertyChange("pvAddress", this.pvAddress, this.pvAddress = pvAddress);
	}

    /**
     * @return true if the block is visible
     */
	public boolean isVisible() {
		return visible;
	}

    /**
     * Set whether the block is visible.
     * 
     * @param visible true for visible
     */
	public void setVisible(boolean visible) {
		firePropertyChange("visible", this.visible, this.visible = visible);
	}

    /**
     * @return true if the block is local
     */
	public boolean isLocal() {
		return local;
	}

    /**
     * Set whether the block is local or not.
     * 
     * @param local true for local
     */
	public void setLocal(boolean local) {
		firePropertyChange("local", this.local, this.local = local);
	}
    
    /**
     * Open the PV dialog window.
     */
    public void openPvDialog() {
		PvSelectorDialog pvDialog = new PvSelectorDialog(null, config, pvAddress);	
		if (pvDialog.open() == Window.OK) {
			setPvAddress(pvDialog.getPVAddress());
		}
    }
	
    /**
     * Update the underlying block with the new values. *
     */
    public void updateBlock() {
    	editingBlock.setName(name);
    	editingBlock.setPV(pvAddress);

    	editingBlock.setIsLocal(local);
    	editingBlock.setIsVisible(visible);
    }
    
    /**
     * Validates the current name and pvAddress and fires an error property
     * change if required.
     */
    public void validate() {
        validate(name, pvAddress);
    }

    private void validate(String name, String pvAddress) {
    	BlockNameValidator nameVal = new BlockNameValidator(config, editingBlock);
        PvValidator addressValid = new PvValidator();
    	BlockRules currentRules = Configurations.getInstance().variables().blockRules.getValue();
        
    	if (!(nameVal.isValidName(name, currentRules))) {
    		setError(true, nameVal.getErrorMessage());
    		setWarning(false, null);
    	} else if (!(addressValid.validatePvAddress(pvAddress))) {
    		setError(true, addressValid.getErrorMessage());
    		setWarning(false, null);
    	} else if (!(addressValid.warningPvAddress(pvAddress))) {
    	    setWarning(true, addressValid.getWarningMessage());
    	    setError(false, null);
    	} else {
    		setError(false, null);
    		setWarning(false, null);
    	}
    }

    /**
     * Checks if the block address entered is in the list of facility PVs, and
     * sets local to false if it is.
     * 
     * @param pvAddress the PV address being entered for the block
     */
    private void checkFacilityPv(String pvAddress) {
        if (facilityPvList.contains(pvAddress)) {
            setLocal(false);
        }
    }

    /**
     * @return a list of the facility PVs for easy searching.
     */
    private List<String> getFacilityPVs() {
        var variables = Configurations.getInstance().variables();
        var facilityInterestPVs =  variables.facilityInterestPVs;
        var value = facilityInterestPVs.getValue();
        String facilityPVsString = value.toString();
        facilityPVsString = facilityPVsString.substring(1, facilityPVsString.length() - 1);
        String[] facilityPVsArray = facilityPVsString.split(", ");
        return Arrays.asList(facilityPVsArray);
    }
}
