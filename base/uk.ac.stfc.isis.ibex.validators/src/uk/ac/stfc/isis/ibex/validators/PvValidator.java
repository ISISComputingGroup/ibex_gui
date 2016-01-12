
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
public class PvValidator {

    public static final String ADDRESS_FORMAT = "PV Address invalid, use only [a-z], [A-Z], [0-9], : and _";
    public static final String ADDRESS_EMPTY = "PV Address invalid, must not be empty";    
    private static final String NO_ERROR = "";

    private String errorMessage;

    /**
     * Creates a PV validator initialised with no error message.
     * 
     */
    public PvValidator() {
        this.errorMessage = NO_ERROR;
    }

    /**
     * Checks that a String conforms to the requirements of a PV address Can
     * only contain alphanumeric, underscore and colon Case is not specified as
     * PVs are not necessarily uppercase - e.g. non instrument prefixes
     * 
     * @param pvAddress
     * @return Boolean addressValid
     */
    public Boolean validatePvAddress(String pvAddress) {
        boolean is_valid = false;

        if (!(pvAddress.matches("^[a-zA-Z0-9_:]*$"))) {
            setErrorMessage(ADDRESS_FORMAT);
        } else if (pvAddress.isEmpty()) {
        	setErrorMessage(ADDRESS_EMPTY);
        } else {
            is_valid = true;
            setErrorMessage(NO_ERROR);
        }

        return is_valid;
    }

    private void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
