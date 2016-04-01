
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

public class InstrumentNameValidator {
    public static final String NAME_FORMAT = "Instrument Name invalid, must either be a known name or start with \"NDW\" and use only [a-z], [A-Z], [0-9], and _";
    public static final String NAME_EMPTY = "Instrument Name invalid, must not be empty";
    private static final String NO_ERROR = "";
    private static final String PREFIX = "NDW";

    private String errorMessage;
    private Collection<String> knownValidNames;

    public InstrumentNameValidator(Collection<String> knownValidNames) {
        errorMessage = NO_ERROR;
        this.knownValidNames = knownValidNames;
    }
    
    /**
     * Checks that a String conforms to the requirements of an instrument name
     * Can only contain alphanumeric and underscore Case is not specified
     * 
     * @param instrumentName
     * @return Boolean instrumentNameValid
     */
    public Boolean validateInstrumentName(String instrumentName) {
        boolean isValid = false;
        
        if (instrumentName.isEmpty()) {
            setErrorMessage(NAME_EMPTY);
        } else if (nameIsKnown(instrumentName)) {
            isValid = true;
            setErrorMessage(NO_ERROR);
        } else if (!(instrumentName.matches(PREFIX + "[a-zA-Z0-9_]*$"))) {
            setErrorMessage(NAME_FORMAT);
        } else {
            isValid = true;
            setErrorMessage(NO_ERROR);
        }

        return isValid;
    }

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
