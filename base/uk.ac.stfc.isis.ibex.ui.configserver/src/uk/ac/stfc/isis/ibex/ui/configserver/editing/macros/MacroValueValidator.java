
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.macros;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class MacroValueValidator extends ModelObject implements IValidator {
	private static final String NO_MACRO_SELECTED = "";
	private static final String EMPTY_VALUE_MESSAGE = "Macro value must not be empty";
	private static final String PATTERN_MISMATCH_MESSAGE = "Macro value must match regex pattern";
	private static final String PATTERN_INVALID = "Macro regex pattern invalid";
	
	private final Label messageDisplayer;
	private Macro macro;
	
	public MacroValueValidator(Macro macro, Label messageDisplayer) {
		this.messageDisplayer = messageDisplayer;
		this.macro = macro;
	}
	
	@Override
	public IStatus validate(Object text) {		
		messageDisplayer.setText("");
		
		if (macro == null) {
			return setError(NO_MACRO_SELECTED);
		}
	
		if (text.equals("")) {
			return setError(EMPTY_VALUE_MESSAGE);	
		}

		boolean matches = matchesPattern((String) text);
		
		if (!matches) {
			return setError(PATTERN_MISMATCH_MESSAGE);
		}
		
		return ValidationStatus.ok();
	}

	private IStatus setError(String message) {
		messageDisplayer.setText(message);
		return ValidationStatus.error(message);	
	}
	
	private boolean matchesPattern(String text) {
		String pattern = macro.getPattern();
		
		if (pattern == null || pattern.isEmpty()) {
			return true;
		}
		
		try {
			return Pattern.matches(pattern, text);
		} catch (PatternSyntaxException e) {
			setError(PATTERN_INVALID);
			return true;
		}
	}
	
	public void setMacro(Macro macro) {
		this.macro = macro;
	}
}
