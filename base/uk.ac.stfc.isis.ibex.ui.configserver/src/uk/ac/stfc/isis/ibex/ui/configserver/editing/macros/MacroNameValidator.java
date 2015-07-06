
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

import java.util.Collection;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;

public class MacroNameValidator implements IValidator {
	private static final String DUPLICATE_MACRO_MESSAGE = "Duplicate macro name: ";
	private static final String EMPTY_NAME_MESSAGE = "Macro name must not be empty";
	private static final String CONTAINS_SPACE_MESSAGE = "Macro name cannot contain a space";
	
	private final Collection<Macro> macros;
	private final Macro selectedMacro;
	private final MessageDisplayer messageDisplayer;
	
	public MacroNameValidator(Collection<Macro> macros, Macro selectedMacro, MessageDisplayer messageDisplayer) {
		this.macros = macros;
		this.selectedMacro = selectedMacro;
		this.messageDisplayer = messageDisplayer;
	}
	
	@Override
	public IStatus validate(Object text) {
		messageDisplayer.setErrorMessage("MacroNameValidator", null);
		
		if (text.equals("")) {
			return setError(EMPTY_NAME_MESSAGE);	
		}
		
		if (macros == null) {
			return ValidationStatus.ok();
		}
		
		if (nameIsDuplicated(text)) {
			return setError(DUPLICATE_MACRO_MESSAGE + ": " + text);
		}
		
		if (((String)text).contains(" ")) {
			return setError(CONTAINS_SPACE_MESSAGE);
		}
		
		return ValidationStatus.ok();
	}

	private IStatus setError(String message) {
		messageDisplayer.setErrorMessage("MacroNameValidator", message);
		return ValidationStatus.error(message);	
	}
	
	private boolean nameIsDuplicated(Object text) {
		//if (selectedMacro!=null) {
			for (Macro macro : macros) {
				if(isNotMacroBeingEdited(macro)) {
					if (macro.getName().equals(text)) {
						return true;
					}
				}
			}
		//}
		
		return false;
	}

	private boolean isNotMacroBeingEdited(Macro macro) {
		return !macro.equals(selectedMacro);
	}
}
