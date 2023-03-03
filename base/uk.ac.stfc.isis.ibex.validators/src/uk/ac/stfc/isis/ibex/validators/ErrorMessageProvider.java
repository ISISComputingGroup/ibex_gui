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

package uk.ac.stfc.isis.ibex.validators;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Provides error message.
 *
 */
public abstract class ErrorMessageProvider extends ModelObject {
	/**
	 * The error.
	 */
    protected ErrorMessage error = new ErrorMessage();
    
    /**
     * The warning.
     */
    protected WarningMessage warning = new WarningMessage();
    
    /**
     * Sets warning to listener.
     *
     *@param inError
     *                  true if there is an error.
     *
     *@param message
     *                  the warning message.
     */
    protected void setError(boolean inError, String message) {
    	firePropertyChange("error", this.error, this.error = new ErrorMessage(inError, message));
    }
    
    /**
     * Returns the error.
     *
     *@return
     *       the error.
     */
    public ErrorMessage getError() {
    	return error;
    }
    
    /**
     * Sets warning to listener.
     * 
     * @param inWarning
     *                  true if there is a warning.
     *
     *@param message
     *                  the warning message.
     */
    public void setWarning(boolean inWarning, String message) {
        firePropertyChange("warning", this.warning, this.warning = new WarningMessage(inWarning, message));
    }
    
    /**
     * Returns the warning.
     *
     *@return
     *       the warning.
     */
    public WarningMessage getWarning() {
        return warning;
    }
}
