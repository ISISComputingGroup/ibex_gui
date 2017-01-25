
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.pvs;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import uk.ac.stfc.isis.ibex.configserver.configuration.PVDefaultValue;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs.IocViewModel;
import uk.ac.stfc.isis.ibex.validators.MessageDisplayer;

/**
 * Checks the validity of a name given to a default PV value.
 */
public class PVNameValidator implements IValidator {
	private static final String DUPLICATE_GROUP_MESSAGE = "Duplicate PV name";
	private static final String EMPTY_NAME_MESSAGE = "PV name must not be empty";
	
	private final EditableIoc ioc;
    private final IocViewModel viewModel;
	private final PVDefaultValue selectedPV;
	private final MessageDisplayer messageDisplayer;
	
    /**
     * Constructor for the validator.
     * 
     * @param viewModel
     *            The Viewmodel of the current IOC.
     * @param selectedPV
     *            The selected PV value.
     * @param messageDisplayer
     *            The dialog used for displaying error messages.
     */
    public PVNameValidator(IocViewModel viewModel, PVDefaultValue selectedPV, MessageDisplayer messageDisplayer) {
        this.viewModel = viewModel;
        this.ioc = viewModel.getIoc();
		this.selectedPV = selectedPV;
		this.messageDisplayer = messageDisplayer;
	}
	
	@Override
	public IStatus validate(Object text) {
		messageDisplayer.setErrorMessage("PVNameValidator", null);
		
		if (text.equals("")) {
			return setError(EMPTY_NAME_MESSAGE);	
		}
		
		if (ioc == null) {
			return ValidationStatus.ok();
		}
		
		if (nameIsDuplicated(text)) {
			return setError(DUPLICATE_GROUP_MESSAGE + ": " + text);
		}
		
		return ValidationStatus.ok();
	}

	private IStatus setError(String message) {
		messageDisplayer.setErrorMessage("PVNameValidator", message);
		return ValidationStatus.error(message);	
	}
	
	private boolean nameIsDuplicated(Object text) {
        for (PVDefaultValue pv : viewModel.getPvVals()) {
			if (isNotPVBeingEdited(pv)) {
				if (pv.getName().equals(text)) {
					return true;
				}
			}
		}
		
		return false;
	}

	private boolean isNotPVBeingEdited(PVDefaultValue pv) {
		return !pv.equals(selectedPV);
	}
}
