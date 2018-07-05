
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
 *
 */
public class PvValidator {

    /**
     * Message displayed when the PV name in invalid.
     */
    public static final String ADDRESS_FORMAT =
            "PV Address invalid, use only letters, numbers, underscores, dashes and colons";

    /**
     * Message displayed when the PV name is empty.
     */
    public static final String ADDRESS_EMPTY = "PV Address invalid, must not be empty";    
    
    /**
     * Message displayed when the PV name contains ":SP".
     */
    public static final String ADDRESS_SP = "Block should usually point at the measured quantity. Doing cset on the block will set the SP value if it exists.";
    
    /**
     * Message displayed when the PV name contains ":SP:RBV".
     */
    public static final String ADDRESS_SP_RBV = "The setpoint value read back from the device is not a measurement of the value but the value the device is trying to get to.";

    /**
     * Message displayed when the PV name contains ":CS:SB".
     */
    public static final String ADDRESS_CS_SB = "This is an IBEX internal pv are you sure you want a block pointing at it?";
    
    /**
     * Message displayed when the PV name is correct.
     */
    public static final String NO_ERROR = "";
    
    private String errorMessage;
    
    private String warningMessage;

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
     * PVs are not necessarily upper case - e.g. non instrument prefixes
     * 
     * @param pvAddress the address to validate
     * @return Boolean addressValid
     */
    public Boolean validatePvAddress(String pvAddress) {
        boolean isValid = false;

        if (pvAddress.isEmpty()) {
            setErrorMessage(ADDRESS_EMPTY);
        } else if (!(pvAddress.matches("^[a-zA-Z0-9_\\-:]+(\\.[a-zA-Z0-9_:\\-]+)?$"))
                || pvAddress.matches(".*(:)\\1+.*")) {
            setErrorMessage(ADDRESS_FORMAT);
        } else {
            isValid = true;
            setErrorMessage(NO_ERROR);
        }

        return isValid;
    }
    
    /**
     * Checks if the Pv Name ends with ":SP", ":SP:RBV" or contains "CS:SB" as these are typical of bad PVs.
     * 
     * @param pvAddress the address to validate
     * @return Boolean addressValid
     */
    public Boolean warningPvAddress(String pvAddress) {
        boolean isValid = false;

        if (pvAddress.endsWith(":SP")) {
            setWarningMessage(ADDRESS_SP);
        } else if (pvAddress.endsWith(":SP:RBV")) {
            setWarningMessage(ADDRESS_SP_RBV);
        } else if (pvAddress.contains("CS:SB")) {
            setWarningMessage(ADDRESS_CS_SB);
        } else {
            isValid = true;
            setWarningMessage(NO_ERROR);
        }

        return isValid;
    }

    private void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the last error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    private void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    /**
     * @return the last warning message
     */
    public String getWarningMessage() {
        return warningMessage;
    }

}
