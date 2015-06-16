package uk.ac.stfc.isis.ibex.ui.configserver.editing.summary;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;

public class SummaryDescriptionValidator implements IValidator {

	private final MessageDisplayer messageDisplayer;
	
	public SummaryDescriptionValidator(MessageDisplayer messageDisplayer) {
		this.messageDisplayer = messageDisplayer;
	}
	
	@Override
	public IStatus validate(Object text) {		
		String str = text.toString().trim();
		
		if (str.isEmpty()) {
			setError("Description cannot be empty");
		}

		messageDisplayer.setErrorMessage("SummaryDescriptionValidator", null);
		return ValidationStatus.ok();
	}
	
	private IStatus setError(String message) {
		messageDisplayer.setErrorMessage("SummaryDescriptionValidator", message);
		return ValidationStatus.error(message);	
	}

}
