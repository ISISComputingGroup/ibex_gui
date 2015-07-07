
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
