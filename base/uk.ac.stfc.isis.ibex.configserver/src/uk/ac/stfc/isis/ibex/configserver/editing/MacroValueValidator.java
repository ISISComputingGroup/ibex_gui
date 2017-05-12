
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

package uk.ac.stfc.isis.ibex.configserver.editing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.validators.ErrorMessageProvider;

/**
 * Verifies an entered macro value and provides an error message if it does not
 * match the given macro.
 */
public class MacroValueValidator extends ErrorMessageProvider {
    /**
     * The message that is displayed when there is no error.
     */
	public static final String NO_MESSAGE = "";
    /**
     * The message that is displayed when the macro does not match it's regex
     * pattern.
     */
	public static final String PATTERN_MISMATCH_MESSAGE = "Macro value must match the pattern shown";
    /**
     * The message that is displayed when the regex pattern is invalid.
     */
	public static final String PATTERN_INVALID = "Macro regex pattern invalid";
	
	private Macro macro;
	
    /**
     * Constructor for the validator.
     * 
     * @param macro
     *            The macro to validate.
     */
    public MacroValueValidator(Macro macro) {
        if (macro != null) {
            macro.addPropertyChangeListener("value", new PropertyChangeListener() {

                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    validateValue((String) evt.getNewValue());
                }
            });
        }
	}
	
    private void validateValue(String value) {
		try {
            if (value.equals("")) {
                setError(false, null);
            } else if (!matchesPattern(value)) {
                setError(true, PATTERN_MISMATCH_MESSAGE);
			} else {
                setError(false, null);
			}
		} catch (PatternSyntaxException e) {
            setError(true, PATTERN_INVALID);
		}			
	}
	
    private boolean matchesPattern(String text) {
		String pattern = macro.getPattern();
		
		if (pattern == null || pattern.isEmpty()) {
			return true;
		}

		return Pattern.matches(pattern, text);
	}
	
    /**
     * Set the macro that this validator is looking at.
     * 
     * @param macro
     *            The macro to validate.
     */
	public void setMacro(Macro macro) {
		this.macro = macro;
	}
}
