
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.validators;

/**
 * Provides validation for PV items
 *
 */
public class RunControlValidator {

    public static final String HIGH_LIMIT_FLOAT = "The high run control limit must be a decimal";
    public static final String HIGH_LIMIT_EMPTY = "The high run control limit must not be empty";
    
    public static final String LOW_LIMIT_FLOAT = "The low run control limit must be a decimal";
    public static final String LOW_LIMIT_EMPTY = "The low run control limit must not be empty";
    
    public static final String LOW_LIMIT_LESS = "The high run control limit must be greater than the low limit";

    public static final String NO_ERROR = "";
    
    private String errorMessage;

    public RunControlValidator() {
        this.errorMessage = NO_ERROR;
    }

	public boolean isValid(String lowLimitText, String highLimitText) {
		boolean isValid = false;
		float tmpLowLimit;
		float tmpHighLimit;
		
		if (lowLimitText == null || highLimitText == null) {
			setErrorMessage(NO_ERROR);
			return false;
		}
		
		try {
			tmpLowLimit = Float.parseFloat(lowLimitText);
		} catch (NumberFormatException e) {
	    	if (lowLimitText.isEmpty()) {
	    		setErrorMessage(LOW_LIMIT_EMPTY);
	    	} else {
	    		setErrorMessage(LOW_LIMIT_FLOAT);
	    	}
	    	return false;
		}
		
		try {
			tmpHighLimit = Float.parseFloat(highLimitText);
		} catch (NumberFormatException e) {
	    	if (highLimitText.isEmpty()) {
	    		setErrorMessage(HIGH_LIMIT_EMPTY);
	    	} else {
	    		setErrorMessage(HIGH_LIMIT_FLOAT);
	    	}
	    	return false;
		}
		
    	if (tmpLowLimit > tmpHighLimit) {
    		setErrorMessage(LOW_LIMIT_LESS);
        } else {
            isValid = true;
            setErrorMessage(NO_ERROR);
        }
    	return isValid;
    	
	}

    private void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
