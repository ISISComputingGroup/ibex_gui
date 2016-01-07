
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
import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.Block;

/**
 * This provides validation for block names. The validation checks for an empty block name, a name that starts with
 * something other than [a-zA-Z], a name that contains characters other than [a-zA-Z_0-9] and duplicated block names.
 * An error message can be obtained if the validation fails.
 * 
 */
public class BlockNameValidator {
	
	public static final String DUPLICATE_GROUP_MESSAGE = "Duplicate block name";
	public static final String EMPTY_NAME_MESSAGE = "Block name must not be empty";
	public static final String INVALID_START_CHAR = "Name should start with a letter";
	public static final String INVALID_CHARS_MESSAGE = "Block name should only contain letters, numbers and underscores";
	public static final String FORBIDDEN_NAME_MESSAGE = "Block name cannot be ";
	public static final String REGEX_FORBIDDEN_MESSAGE = "Block name must match the regex";
	private static final String NO_ERROR = "";
	
	private final EditableConfiguration config;
	private final Block selectedBlock;
	
	private String errorMessage;
	
	/**
	 * Creates a block name validator for a specific config and block, initialised with no error message.
	 * 
	 * @param config The configuration being edited
	 * @param selectedBlock - The block being created or edited
	 */
	public BlockNameValidator(EditableConfiguration config, Block selectedBlock) {
		this.config = config;
		this.selectedBlock = selectedBlock;
		this.errorMessage = NO_ERROR;
	}
	
	/**
	 * Checks the validity of a proposed block name, and sets the error message if the
	 * name is invalid.
	 * 
	 * @param name The proposed name
	 * @return True if the name is valid, else false with the error message set
	 */
	public Boolean isValidName(String name) {
		
		boolean is_valid = false;
		BlockRules block_rules = Configurations.getInstance().variables().blockRules.getValue();

		if (name.equals("")) {
			setErrorMessage(EMPTY_NAME_MESSAGE);	
		} else if (nameIsDuplicated(name)) {
			setErrorMessage(DUPLICATE_GROUP_MESSAGE + ": " + name);
		} else if (!startsWithLetter(name)) {
			setErrorMessage(INVALID_START_CHAR);
		} else if (containsSpecialCharacters(name)) {
			setErrorMessage(INVALID_CHARS_MESSAGE);
		} else if ((block_rules != null) && (nameIsForbidden(name, block_rules))) {
			setErrorMessage(FORBIDDEN_NAME_MESSAGE + getListAsString(block_rules.getDisallowed()));
		} else if ((block_rules != null) && !(name.matches(block_rules.getRegex()))) {
			setErrorMessage(REGEX_FORBIDDEN_MESSAGE + ": " + block_rules.getRegex());
		} else {
			is_valid = true;
			setErrorMessage(NO_ERROR);
		}
		return is_valid;
	}

	private void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	private boolean nameIsForbidden(String text, BlockRules blockRules) {
		boolean is_valid = false;
		for (String forbidden : blockRules.getDisallowed())
			is_valid |= text.toLowerCase().equals(forbidden.toLowerCase());
		return is_valid;
	}
	
	private String getListAsString(List<String> list){
		StringBuilder sb = new StringBuilder();		
		for(int i = 0; i < list.size(); i++)
		{
			sb.append(list.get(i));
			if(i < list.size() - 2)
				sb.append(", ");
			else if (i == list.size() - 2)
				sb.append(" or ");
		}
		return sb.toString();
	}
	
	private boolean nameIsDuplicated(String text) {
		for (EditableBlock block : config.getEditableBlocks()) {
			if (isNotSelectedBlock(block) && block.getName().equals(text)) {
				return true;
			}
		}		
		return false;
	}

	private boolean isNotSelectedBlock(Block block) {
		return !block.equals(selectedBlock);
	}
	
	private Boolean startsWithLetter(String name) {
		return name.substring(0, 1).matches("[a-zA-Z]");
	}
	
    private Boolean containsSpecialCharacters(String name) {
		return !name.matches("^[a-zA-Z0-9_]*$");
    }
}
