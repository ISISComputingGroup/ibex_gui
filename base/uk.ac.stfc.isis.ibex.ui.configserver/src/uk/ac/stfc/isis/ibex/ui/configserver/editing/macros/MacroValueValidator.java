package uk.ac.stfc.isis.ibex.ui.configserver.editing.macros;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;

public class MacroValueValidator extends ModelObject implements IValidator {
	private static final String EMPTY_VALUE_MESSAGE = "Macro value must not be empty";
	private static final String PATTERN_MISMATCH_MESSAGE = "Macro value must match regex pattern";
	private static final String PATTERN_INVALID = "Macro regex pattern invalid";
	
	private final MessageDisplayer messageDisplayer;
	private final Macro macro;
	
	public MacroValueValidator(Macro macro, MessageDisplayer messageDisplayer) {
		this.messageDisplayer = messageDisplayer;
		this.macro = macro;
	}
	
	@Override
	public IStatus validate(Object text) {
		messageDisplayer.setErrorMessage("MacroValueValidator", null);
		
		if (text.equals("")) {
			return setError(EMPTY_VALUE_MESSAGE);	
		}
		
		if (!matchesPattern((String)text)) {
			return setError(PATTERN_MISMATCH_MESSAGE);
		}
		
		return ValidationStatus.ok();
	}

	private IStatus setError(String message) {
		messageDisplayer.setErrorMessage("MacroValueValidator", message);
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
}
