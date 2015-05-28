package uk.ac.stfc.isis.ibex.ui.configserver.editing.groups;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableGroup;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;

public class GroupNameValidator implements IValidator {
	
	private static final String DUPLICATE_GROUP_MESSAGE = "Duplicate group name";
	private static final String EMPTY_NAME_MESSAGE = "Group name must not be empty";
	
	private final EditableConfiguration config;
	private final IObservableValue selectedGroup;
	private final MessageDisplayer messageDisplayer;
	
	public GroupNameValidator(EditableConfiguration config, IObservableValue selectedGroup, MessageDisplayer messageDisplayer) {
		this.config = config;
		this.selectedGroup = selectedGroup;
		this.messageDisplayer = messageDisplayer;
	}
	
	@Override
	public IStatus validate(Object text) {
		messageDisplayer.setErrorMessage("GroupNameValidator", null);
		
		if (text == "") {
			return setError(EMPTY_NAME_MESSAGE);	
		}
		
		if (config == null) {
			return ValidationStatus.ok();
		}
		
		if (nameIsDuplicated(text)) {
			return setError(DUPLICATE_GROUP_MESSAGE);
		}
		
		return ValidationStatus.ok();
	}

	private IStatus setError(String message) {
		messageDisplayer.setErrorMessage("GroupNameValidator", message);
		return ValidationStatus.error(message);	
	}
	
	private boolean nameIsDuplicated(Object text) {
		for (EditableGroup group : config.getEditableGroups()) {
			if(isNotGroupBeingEdited(group)) {
				if (group.getName().equals(text)) {
					return true;
				}
			}
		}
		
		return false;
	}

	private boolean isNotGroupBeingEdited(EditableGroup group) {
		return !group.equals(selectedGroup.getValue());
	}
}
