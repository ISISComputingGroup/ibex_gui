
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

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

/**
 * Validator to ensure that group names are correct.
 */
public class GroupNameValidator implements IValidator {
    
    /** logger. */
    private static final Logger LOG = LogManager.getLogger(GroupNameValidator.class);
	
    /** Message to issue on duplicate group name. */
    private static final String DUPLICATE_GROUP_MESSAGE = "Group names must all be unique";
	
    /** provider of group names. */
    private final GroupNamesProvider groupNameProvider;

    /** Error message displayer. */
	private final MessageDisplayer messageDisplayer;

    /** group rules. */
    private final BlockServerNameValidation groupRules;
	
    /**
     * Instantiates a new group name validator.
     *
     * @param groupNameProvider a provider of the group names in the
     *            configuration
     * @param messageDisplayer the message displayer
     * @param groupRules the group validation rules
     */
    public GroupNameValidator(GroupNamesProvider groupNameProvider, MessageDisplayer messageDisplayer,
            BlockServerNameValidation groupRules) {
        this.groupNameProvider = groupNameProvider;
		this.messageDisplayer = messageDisplayer;
        this.groupRules = groupRules;
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
	        messageDisplayer.setErrorMessage("GroupNameValidator", null);
            LOG.error("Group rules are null and should not be");
            return ValidationStatus.ok();
		}
	    
        String what = "Current group";
        IStatus status = groupRules.validate((String) text, what);
        if (!status.isOK()) {
            messageDisplayer.setErrorMessage("GroupNameValidator", status.getMessage());
            return status;
        }

        if (groupNameProvider != null) {
            for (String name : groupNameProvider.getGroupNames()) {
                status = groupRules.validate(name, "Group");
                if (!status.isOK()) {
                    messageDisplayer.setErrorMessage("GroupNameValidator", status.getMessage());
                    return status;
                }
            }

            if (groupNameProvider != null && listContainsDuplicates()) {
                messageDisplayer.setErrorMessage("GroupNameValidator", DUPLICATE_GROUP_MESSAGE);
                return ValidationStatus.error(DUPLICATE_GROUP_MESSAGE);
            }
        }

        messageDisplayer.setErrorMessage("GroupNameValidator", null);
        return ValidationStatus.ok();
	}
	
    /**
     * @return true, if list contains duplicates; false otherwise
     */
    private boolean listContainsDuplicates() {
        Set<String> uniques = new HashSet<>();

        for (String name : groupNameProvider.getGroupNames()) {
            if (!uniques.add(name.toLowerCase())) {
                return true;
            }
        }
        return false;
	}

}
