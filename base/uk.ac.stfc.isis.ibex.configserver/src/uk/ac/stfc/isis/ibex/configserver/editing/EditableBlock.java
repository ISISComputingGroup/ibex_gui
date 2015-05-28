package uk.ac.stfc.isis.ibex.configserver.editing;

import uk.ac.stfc.isis.ibex.configserver.configuration.Block;

public class EditableBlock extends Block {

	public EditableBlock(Block other) {
		super(other);
	}

	public boolean isEditable() {
		return !hasSubConfig();
	}
}
