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
