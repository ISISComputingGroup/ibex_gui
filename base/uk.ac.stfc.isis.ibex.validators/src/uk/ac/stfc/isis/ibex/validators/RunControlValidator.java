
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
 * Provides validation for PV items.
 */
public class RunControlValidator {
    
    /** 
     * The error message displayed if the low limit is higher than the high limit.    
     */
    public static final String LOW_LIMIT_LESS = "The high run control limit must be greater than the low limit";
    
    /** 
     * The error message displayed if the limits are set using something other than numbers.    
     */
    public static final String INVALID_LIMIT = "The run control limits must be numbers";
    
    /** 
     * The message displayed if they are no errors.    
     */
    public static final String NO_ERROR = "";
    
    private String errorMessage;
    
    /**
     * A validator that checks if the run control inputs are valid and creates an error message if they aren't.
     */
    public RunControlValidator() {
        this.errorMessage = NO_ERROR;
    }
    
    /**
     * Checks if the current inputs are valid.
     * 
     * @param lowLimit
     *                  The low limit input.
     * @param highLimit
     *                  The high limit input.
     * @param enabled
     *                  True if the run control is enabled.
     * @return
     *                  True if all the inputs are valid.
     */
	public boolean isValid(Double lowLimit, Double highLimit, boolean enabled) {
		if (!enabled) {
			// Run control not enabled - valid no matter what the limits are.
			setErrorMessage(null);
			return true;
		} else if (lowLimit == null || highLimit == null) {
			setErrorMessage(INVALID_LIMIT);
			return false;
		} else if (lowLimit > highLimit) {
    		setErrorMessage(LOW_LIMIT_LESS);
    		return false;
        } else {
            setErrorMessage(NO_ERROR);
            return true;
        }
    	
	}

    private void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    /**
     * Returns the current error message.
     * 
     * @return
     *          The current error message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

}
