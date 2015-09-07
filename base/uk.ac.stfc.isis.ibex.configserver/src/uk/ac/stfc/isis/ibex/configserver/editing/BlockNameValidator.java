
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

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;

public class BlockNameValidator {
	
	private static final String DUPLICATE_GROUP_MESSAGE = "Duplicate block name";
	private static final String EMPTY_NAME_MESSAGE = "Block name must not be empty";
	private static final String INVALID_START_CHAR = "Name should start with a letter";
	private static final String INVALID_CHARS_MESSAGE = "Block name must not contain special characters";
	
	private final EditableConfiguration config;
	private final Block selectedBlock;
	
	private String errorMessage;
	
	public BlockNameValidator(EditableConfiguration config, Block selectedBlock) {
		this.config = config;
		this.selectedBlock = selectedBlock;
		this.errorMessage = "";
	}
	
	public Boolean validate(String text) {		
		if (text.equals("")) {
			setError(EMPTY_NAME_MESSAGE);	
			return false;
		}
		
		if (nameIsDuplicated(text)) {
			setError(DUPLICATE_GROUP_MESSAGE + ": " + text);
			return false;
		}
		
		if (!validateFirstCharacterOfName((String)text)) {
			setError(INVALID_START_CHAR);
			return false;
		}
		
		if (!validateForSpecialCharacters((String)text)) {
			setError(INVALID_CHARS_MESSAGE);
			return false;
		}
			
		return true;
	}

	private void setError(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getError() {
		return errorMessage;
	}
	
	private boolean nameIsDuplicated(Object text) {
		for (EditableBlock block : config.getEditableBlocks()) {
			if(isNotBlockBeingEdited(block)) {
				if (block.getName().equals(text)) {
					return true;
				}
			}
		}
		
		return false;
	}

	private boolean isNotBlockBeingEdited(Block block) {
		return !block.equals(selectedBlock);
	}
	
	private Boolean validateFirstCharacterOfName(String name) {
		return name.substring(0, 1).matches("[a-zA-Z]");
	}
	
    private Boolean validateForSpecialCharacters(String name) {
		return name.matches("^[a-zA-Z0-9_]*$");
    }
}
