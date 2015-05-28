package uk.ac.stfc.isis.ibex.configserver.editing;

import uk.ac.stfc.isis.ibex.configserver.configuration.PVSet;

public class EditablePVSet extends PVSet {
	private String description;
	
	public EditablePVSet(PVSet other, String description) {
		super(other);
		this.description = description;
	}
	
	public EditablePVSet(String name, boolean editable, String description) {
		super(name, editable);
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
