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
