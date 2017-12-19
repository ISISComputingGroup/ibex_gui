
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

package uk.ac.stfc.isis.ibex.configserver.editing;

import java.util.List;

import uk.ac.stfc.isis.ibex.configserver.BlockRules;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;

/**
 * This provides validation for block names. The validation checks for an empty block name, a name that starts with
 * something other than [a-zA-Z], a name that contains characters other than [a-zA-Z_0-9] and duplicated block names.
 * An error message can be obtained if the validation fails.
 * 
 */
public class BlockNameValidator {
    /**
     * The error to display when the name of the block is already taken.
     */
	public static final String DUPLICATE_GROUP_MESSAGE = "Duplicate block name";
    /**
     * The error to display when the block name is empty.
     */
	public static final String EMPTY_NAME_MESSAGE = "Block name must not be empty";
    /**
     * The error to display when the block name is not allowed.
     */
	public static final String FORBIDDEN_NAME_MESSAGE = "Block name cannot be ";
	private static final String NO_ERROR = "";
	
	private String errorMessage;
	
    private final EditableConfiguration config;
    private final Block selectedBlock;
	
	/**
	 * Creates a block name validator for a specific config and block, initialised with no error message.
	 * 
	 * @param config The configuration being edited
	 * @param selectedBlock - The block being created or edited
	 */
	public BlockNameValidator(EditableConfiguration config, Block selectedBlock) {
		this.errorMessage = NO_ERROR;
		this.config = config;
		this.selectedBlock = selectedBlock;
	}
	
	/**
     * Checks the validity of a proposed block name, and sets the error message
     * if the name is invalid.
     * 
     * @param name
     *            The proposed name
     * @param blockRules
     *            The rules for naming blocks
     * @return True if the name is valid, else false with the error message set
     */
	public Boolean isValidName(String name, BlockRules blockRules) {
		boolean isValid = false;
		
		if (name.equals("")) {
			setErrorMessage(EMPTY_NAME_MESSAGE);	
		} else if (nameIsDuplicated(name)) {
			setErrorMessage(DUPLICATE_GROUP_MESSAGE + ": " + name);
		} else if ((blockRules != null) && (nameIsForbidden(name, blockRules))) {
			setErrorMessage(FORBIDDEN_NAME_MESSAGE + getListAsString(blockRules.getDisallowed()));
		} else if ((blockRules != null) && !(name.matches(blockRules.getRegex()))) {
			setErrorMessage(blockRules.getRegexErrorMessage());
		} else {
			isValid = true;
			setErrorMessage(NO_ERROR);
		}
		return isValid;
	}

	private void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	/**
	 * @return The current error message.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
	private boolean nameIsForbidden(String text, BlockRules blockRules) {
		boolean isValid = false;
		for (String forbidden : blockRules.getDisallowed()) {
			isValid |= text.toLowerCase().equals(forbidden.toLowerCase());
		}
		return isValid;
	}
	
	private String getListAsString(List<String> list) {
		StringBuilder sb = new StringBuilder();		
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
			if (i < list.size() - 2) {
				sb.append(", ");
			} else if (i == list.size() - 2) {
				sb.append(" or ");
			}
		}
		return sb.toString();
    }

    private boolean nameIsDuplicated(String name) {
        return nameInBase(name) || nameInComps(name);
    }

    private boolean nameInBase(String name) {
        for (EditableBlock block : config.getOtherBlocks()) {
            if (isNotSelectedBlock(block) && block.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    private boolean nameInComps(String name) {
        if (config.getEditableComponents() == null) {
            return false;
        }
        for (Configuration comp : config.getEditableComponents().getSelected()) {
            for (Block block : comp.getBlocks()) {
                if (block.getName().equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isNotSelectedBlock(Block block) {
        return !block.equals(selectedBlock);
    }
}
