
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

package uk.ac.stfc.isis.ibex.validators;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Validator for the description on a summary of a configuration.
 */
public class SummaryDescriptionValidator implements IValidator {

    /** The logger. */
    private static final Logger LOG = IsisLog.getLogger(SummaryDescriptionValidator.class);

    /** Error message source. */
    private static final String ERROR_MESSAGE_SOURCE = "SummaryDescriptionValidator";

    /** Error message displayer. */
    private final MessageDisplayer messageDisplayer;

    /** Validator of a description from the block server. */
    private BlockServerNameValidor descriptionValidator;
	
    /**
     * Instantiates a class which can validate the description of a
     * configuration summary.
     *
     * @param messageDisplayer the message displayer
     * @param descriptionValidator the description validator
     */
    public SummaryDescriptionValidator(MessageDisplayer messageDisplayer, BlockServerNameValidor descriptionValidator) {
		this.messageDisplayer = messageDisplayer;
        this.descriptionValidator = descriptionValidator;

        if (this.descriptionValidator == null) {
            LOG.error("Configuration description rules are null and should not be");
            this.descriptionValidator = BlockServerNameValidor.getDefaultInstance();
        }

	}
	
	@Override
    public IStatus validate(Object text) {

        String descriptionToValidate = text.toString().trim();
        IStatus status = this.descriptionValidator.validate(descriptionToValidate, "Description");
        
        return setErrorAndReturnStatus(status);
    }

    /**
     * Set the error message.
     * 
     * @param status the status of the error
     * @return the status
     */
    private IStatus setErrorAndReturnStatus(IStatus status) {
        if (messageDisplayer == null) {
            return status;
        }
        if (status.isOK()) {
            messageDisplayer.setErrorMessage(ERROR_MESSAGE_SOURCE, null);
        } else {
            messageDisplayer.setErrorMessage(ERROR_MESSAGE_SOURCE, status.getMessage());
        }
        return status;
    }

}
