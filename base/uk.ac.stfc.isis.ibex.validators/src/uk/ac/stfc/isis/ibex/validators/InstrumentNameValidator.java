
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

import java.util.Collection;

/**
 * Provides validation for custom instrument names.
 * 
 */
public class InstrumentNameValidator {
    /**
     * Error message for wrong instrument name formatting.
     */
    public static final String NAME_FORMAT_MSG =
            "Instrument Name invalid, must only use letters, numbers and underscores.";

    /**
     * Error message for empty instrument name.
     */
    public static final String NAME_EMPTY_MSG = "Instrument Name invalid, must not be empty";

    private static final String NO_ERROR_MSG = "";

    private String errorMessage;
    private Collection<String> knownValidNames;

    /**
     * Creates an instance of the instrument name validator, given a list of
     * known instrument names.
     * 
     * @param knownValidNames the list of known instrument names.
     */
    public InstrumentNameValidator(Collection<String> knownValidNames) {
        errorMessage = NO_ERROR_MSG;
        this.knownValidNames = knownValidNames;
    }
    
    /**
     * Checks that a String conforms to the requirements of an instrument name
     * Can only contain alphanumeric and underscore Case is not specified.
     * 
     * @param instrumentName name of the instrument ro validate
     * @return whether instrument name is valid
     */
    public Boolean validateInstrumentName(String instrumentName) {
        boolean isValid = false;
        
        if (instrumentName.isEmpty()) {
            setErrorMessage(NAME_EMPTY_MSG);
        } else if (nameIsKnown(instrumentName)) {
            isValid = true;
            setErrorMessage(NO_ERROR_MSG);
        } else if (!(instrumentName.matches("[a-zA-Z0-9_]*$"))) {
            setErrorMessage(NAME_FORMAT_MSG);
        } else {
            isValid = true;
            setErrorMessage(NO_ERROR_MSG);
        }

        return isValid;
    }

    /**
     * Gets the validation message.
     * 
     * @return the validation message.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    private void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    private boolean nameIsKnown(String name) {
        for (String knownName : knownValidNames) {
            if (name.equalsIgnoreCase(knownName)) {
                return true;
            }
        }

        return false;
    }
}
