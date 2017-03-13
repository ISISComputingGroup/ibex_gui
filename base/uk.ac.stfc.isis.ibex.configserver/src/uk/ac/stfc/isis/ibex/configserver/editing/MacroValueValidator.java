
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

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Verifies an entered macro value. This implements the IValidator interface for setting
 * an error message, and extends model object to allow property changes on name is valid
 * and show warning icon. The warning icon is not shown if no macro is selected, even 
 * though the status will return error (but with no message displayed).
 *
 */
public class MacroValueValidator extends ModelObject implements IValidator {
	public static final String NAME_IS_VALID = "nameIsValid";
	public static final String SHOW_WARNING_ICON = "showWarningIcon";
	
	public static final String NO_MESSAGE = "";
	public static final String PATTERN_MISMATCH_MESSAGE = "Macro value must match the pattern shown";
	public static final String PATTERN_INVALID = "Macro regex pattern invalid";
	
	private final Label messageDisplayer;
	private Macro macro;
	private boolean nameIsValid = true;
	private boolean showWarningIcon = false;
	
	public MacroValueValidator(Macro macro, Label messageDisplayer) {
		this.messageDisplayer = messageDisplayer;
		this.macro = macro;
	}
	
	@Override
	public IStatus validate(Object text) {
		IStatus returnStatus;
		
		setShowWarningIcon(false);
		
		try {
            if (macro == null) {
                returnStatus = setError(NO_MESSAGE);
            } else if (text.equals("")) {
                returnStatus = setNoError();
            } else if (!matchesPattern((String) text)) {
				setShowWarningIcon(true);
				returnStatus = setError(PATTERN_MISMATCH_MESSAGE);
			} else {
				returnStatus = setNoError();
			}
		} catch (PatternSyntaxException e) {
			setShowWarningIcon(true);
			returnStatus = setError(PATTERN_INVALID);
		}			
		
		return returnStatus;
	}

	private IStatus setError(String message) {
		messageDisplayer.setText(message);
		setNameIsValid(false);
		return ValidationStatus.error(message);	
	}
	
	private IStatus setNoError() {
		messageDisplayer.setText(NO_MESSAGE);
		setNameIsValid(true);
		setShowWarningIcon(false);
		return ValidationStatus.ok();	
	}
	
    private boolean matchesPattern(String text) {
		String pattern = macro.getPattern();
		
		if (pattern == null || pattern.isEmpty()) {
			return true;
		}

		return Pattern.matches(pattern, text);
	}
	
	public void setMacro(Macro macro) {
		this.macro = macro;
	}
	
	public boolean getNameIsValid() {
		return nameIsValid;
	}
	
	public void setNameIsValid(boolean nameIsValid) {
		firePropertyChange(NAME_IS_VALID, this.nameIsValid, this.nameIsValid = nameIsValid);
	}
	
	public boolean getShowWarningIcon() {
		return showWarningIcon;
	}
	
	public void setShowWarningIcon(boolean showWarningIcon) {
		firePropertyChange(SHOW_WARNING_ICON, this.showWarningIcon, this.showWarningIcon = showWarningIcon);
	}	
}
