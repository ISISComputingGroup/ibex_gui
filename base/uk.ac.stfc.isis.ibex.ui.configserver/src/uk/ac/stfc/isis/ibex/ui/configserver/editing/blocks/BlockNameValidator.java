
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

package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableBlock;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.ui.configserver.dialogs.MessageDisplayer;

public class BlockNameValidator implements IValidator {
	
	private static final String DUPLICATE_GROUP_MESSAGE = "Duplicate block name";
	private static final String EMPTY_NAME_MESSAGE = "Block name must not be empty";
	private static final String INVALID_CHARS_MESSAGE = "Block name must not contain special characters";
	
	private final EditableConfiguration config;
	private final Block selectedBlock;
	private final MessageDisplayer messageDisplayer;
	
	public BlockNameValidator(EditableConfiguration config, Block selectedBlock, MessageDisplayer messageDisplayer) {
		this.config = config;
		this.selectedBlock = selectedBlock;
		this.messageDisplayer = messageDisplayer;
	}
	
	@Override
	public IStatus validate(Object text) {
		messageDisplayer.setErrorMessage("BlockNameValidator", null);
		
		if (text.equals("")) {
			return setError(EMPTY_NAME_MESSAGE);	
		}
		
		if (config == null) {
			return ValidationStatus.ok();
		}
		
		if (nameIsDuplicated(text)) {
			return setError(DUPLICATE_GROUP_MESSAGE + ": " + text);
		}
		
		if (!checkText((String)text)) {
			return setError(INVALID_CHARS_MESSAGE);
		}
			
		return ValidationStatus.ok();
	}

	private IStatus setError(String message) {
		messageDisplayer.setErrorMessage("BlockNameValidator", message);
		return ValidationStatus.error(message);	
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
	
	private Boolean checkText(String name) {		
		//Must start with a letter and contain no spaces	
		return name.matches("^[a-zA-Z][a-zA-Z0-9_]*$");
	}
}
