
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
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Validator to ensure that group names are correct.
 */
public class GroupNameValidator implements IValidator {
    
    /** Source to use in the Message displayer. */
    private static final String ERROR_MESSAGE_SOURCE = "GroupNameValidator";

    /** logger. */
    private static final Logger LOG = IsisLog.getLogger(GroupNameValidator.class);
	
    /** What is being validated. */
    private static final String WHAT_IS_BEING_VALIDATED = "Group name";

    /** Message to issue on duplicate group name. */
    private static final String DUPLICATE_GROUP_MESSAGE = "Group names must all be unique";
	
    /** provider of group names. */
    private final GroupNamesProvider groupNameProvider;

    /** Error message displayer. */
	private final MessageDisplayer messageDisplayer;

    /** group rules. */
    private final BlockServerNameValidator groupRules;

    /** index of the select group. */
    private int selectedIndex;
	
    /**
     * Instantiates a new group name validator.
     *
     * @param groupNameProvider a provider of the group names in the
     *            configuration
     * @param messageDisplayer the message displayer
     * @param groupRules the group validation rules
     */
    public GroupNameValidator(GroupNamesProvider groupNameProvider, MessageDisplayer messageDisplayer,
            BlockServerNameValidator groupRules) {
        this.groupNameProvider = groupNameProvider;
		this.messageDisplayer = messageDisplayer;
        this.groupRules = groupRules;
        this.selectedIndex = -1;
	}
	
    /**
     * Validate the group name list and current group name. All group names must
     * be unique and must conform to the block server's regular expression
     * 
     * @param text group name about to set
     * @return error with message if group name is invalid; ok otherwise
     */
	@Override
	public IStatus validate(Object text) {
        
        if (groupRules == null) {
            LOG.error("Group rules are null and should not be");
            return setErrorAndReturnStatus(ValidationStatus.ok());
		}

        // not editing a group so the group name can not be invalid
        if (selectedIndex == -1) {
            return setErrorAndReturnStatus(ValidationStatus.ok());
        }
	    
        IStatus status = groupRules.validate((String) text, WHAT_IS_BEING_VALIDATED);
        if (!status.isOK()) {

            return setErrorAndReturnStatus(status);
        }

        if (groupNameProvider != null) {
            for (String name : groupNameProvider.getGroupNames()) {
                status = groupRules.validate(name, WHAT_IS_BEING_VALIDATED);
                if (!status.isOK()) {
                    return setErrorAndReturnStatus(status);
                }
            }

            if (groupNameProvider != null && isDuplicateName((String) text)) {
                status = ValidationStatus.error(DUPLICATE_GROUP_MESSAGE);
                return setErrorAndReturnStatus(status);
            }
        }

        return setErrorAndReturnStatus(ValidationStatus.ok());
	}

    /**
     * Set the index of the selected group.
     * 
     * @param index new index of selected group; -1 for group not selected
     */
    public void setSelectedIndex(int index) {
        this.selectedIndex = index;
    }

    /**
     * Dose this name match one on the list of names.
     * 
     * @param newGroupName to set
     * @return true is group name is a duplicate; false otherwise
     */
    private boolean isDuplicateName(String newGroupName) {
        int i = 0;
        for (String name : groupNameProvider.getGroupNames()) {
            if (i != selectedIndex && name.equalsIgnoreCase(newGroupName)) {
                return true;
            }
            i++;
        }
        return false;
    }

    /**
     * Set the error message.
     * 
     * @param status the status of the error
     * @return the status
     */
    private IStatus setErrorAndReturnStatus(IStatus status) {
        if (status.isOK()) {
            messageDisplayer.setErrorMessage(ERROR_MESSAGE_SOURCE, null);
        } else {
            messageDisplayer.setErrorMessage(ERROR_MESSAGE_SOURCE, status.getMessage());
        }
        return status;
    }

}
