
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
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;
import uk.ac.stfc.isis.ibex.validators.RunControlValidator;

/**
 * The view model for the run-control settings for a block.
 */
public class BlockRunControlViewModel extends ErrorMessageProvider {    
	private String lowLimitText = "";
	private String highLimitText = "";    
	
	private float lowLimit;
    private float highLimit;
    private boolean enabled;
    
    private final Block editingBlock;
    private final RunControlValidator runControlValidator = new RunControlValidator();
    
    /**
     * Constructor.
     * 
     * @param editingBlock the block being edited
     */
    public BlockRunControlViewModel(final Block editingBlock) {
    	this.editingBlock = editingBlock;
    	
    	setLowLimitText(Float.toString(editingBlock.getRCLowLimit()));
    	setHighLimitText(Float.toString(editingBlock.getRCHighLimit()));
    	enabled = editingBlock.getRCEnabled();
	}
    
    /**
     * @return the low limit
     */
    public String getLowLimitText() {
		return lowLimitText;
	}
    
    /**
     * Set the low limit.
     * 
     * @param lowLimitText the new value
     */
	public void setLowLimitText(String lowLimitText) {
		boolean isValid = runControlValidator.isValid(lowLimitText, highLimitText);
		if (isValid) {
			lowLimit = Float.parseFloat(lowLimitText);
		}
		setError(!(isValid), runControlValidator.getErrorMessage());
		
		firePropertyChange("lowLimitText", this.lowLimitText, this.lowLimitText = lowLimitText);
	}
	
    /**
     * @return the high limit
     */
	public String getHighLimitText() {
		return highLimitText;
	}

    /**
     * Set the high limit.
     * 
     * @param highLimitText the new value
     */
	public void setHighLimitText(String highLimitText) {
		boolean isValid = runControlValidator.isValid(lowLimitText, highLimitText);
		if (isValid) {
			highLimit = Float.parseFloat(highLimitText);
		}
		setError(!(isValid), runControlValidator.getErrorMessage());
		
		firePropertyChange("highLimitText", this.highLimitText, this.highLimitText = highLimitText);
	}
	
    /**
     * @return the low limit
     */
	public float getLowLimit() {
		return lowLimit;
	}
	
    /**
     * @return the high limit
     */
	public float getHighLimit() {
		return highLimit;
	}
	
    /**
     * Set whether run-control is enabled.
     * 
     * @param enabled enable or not
     */
	public void setEnabled(boolean enabled) {
		firePropertyChange("enabled", this.enabled, this.enabled = enabled);
	}
	
    /**
     * @return whether run-control is enabled
     */
	public boolean getEnabled() {
		return enabled;
	}
    
    /**
     * Update the stored settings.
     */
    public void updateBlock() {
    	editingBlock.setRCHighLimit(highLimit);
    	editingBlock.setRCLowLimit(lowLimit);
    	editingBlock.setRCEnabled(enabled);
    }
}
