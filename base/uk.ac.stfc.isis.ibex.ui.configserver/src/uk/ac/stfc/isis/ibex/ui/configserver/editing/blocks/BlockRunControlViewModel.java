
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

public class BlockRunControlViewModel extends ErrorMessageProvider {
    private static String HIGH_LIMIT_FLOAT = "The high run control limit must be a decimal";
    private static String HIGH_LIMIT_EMPTY = "The high run control limit must not be empty";
    
    private static String LOW_LIMIT_FLOAT = "The low run control limit must be a decimal";
    private static String LOW_LIMIT_EMPTY = "The low run control limit must not be empty";
    
    private static String LOW_LIMIT_LESS = "The high run control limit must be greater than the low limit";
    
	private String lowLimitText = "";
	private String highLimitText = "";    
	
	private float lowLimit;
    private float highLimit;
    private boolean enabled;
    
    private final Block editingBlock;
    
    public BlockRunControlViewModel(final Block editingBlock) {
    	this.editingBlock = editingBlock;
    	
    	setLowLimitText(Float.toString(editingBlock.getRCLowLimit()));
    	setHighLimitText(Float.toString(editingBlock.getRCHighLimit()));
    	enabled = editingBlock.getRCEnabled();
	}
    
    public String getLowLimitText() {
		return lowLimitText;
	}
    
	public void setLowLimitText(String lowLimitText) {
		if (isValid(lowLimitText, highLimitText)) {
			lowLimit = Float.parseFloat(lowLimitText);
		}
		firePropertyChange("lowLimitText", this.lowLimitText, this.lowLimitText = lowLimitText);
	}
	
	public String getHighLimitText() {
		return highLimitText;
	}
	
	private boolean isValid(String lowLimitText, String highLimitText) {
		boolean is_valid = false;
		float tmpLowLimit;
		float tmpHighLimit;
		
		try {
			tmpLowLimit = Float.parseFloat(lowLimitText);
		} catch (NumberFormatException e) {
	    	if (lowLimitText.isEmpty())
	    		setError(true, LOW_LIMIT_EMPTY);
	    	else
	    		setError(true, LOW_LIMIT_FLOAT);
	    	return false;
		}
		
		try {
			tmpHighLimit = Float.parseFloat(highLimitText);
		} catch (NumberFormatException e) {
	    	if (highLimitText.isEmpty())
	    		setError(true, HIGH_LIMIT_EMPTY);
	    	else
	    		setError(true, HIGH_LIMIT_FLOAT);
	    	return false;
		}
		
    	if (tmpLowLimit > tmpHighLimit) {
        	setError(true, LOW_LIMIT_LESS);
        } else {
            is_valid = true;
        	setError(false, null);
        }
    	return is_valid;
    	
	}
	
	public void setHighLimitText(String highLimitText) {
		if (isValid(lowLimitText, highLimitText)) {
			highLimit = Float.parseFloat(highLimitText);
		}
		firePropertyChange("highLimitText", this.highLimitText, this.highLimitText = highLimitText);
	}
	
	public float getLowLimit() {
		return lowLimit;
	}
	
	public float getHighLimit() {
		return highLimit;
	}
	
	public void setEnabled(boolean enabled) {
		firePropertyChange("enabled", this.enabled, this.enabled = enabled);
	}
	
	public boolean getEnabled() {
		return enabled;
	}
    
    public void updateBlock() {
    	editingBlock.setRCHighLimit(highLimit);
    	editingBlock.setRCLowLimit(lowLimit);
    	editingBlock.setRCEnabled(enabled);
    }
}
