
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
