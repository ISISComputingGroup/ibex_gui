package uk.ac.stfc.isis.ibex.journal;

import java.util.EnumMap;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class JournalRow extends ModelObject {
	private EnumMap<JournalField, String> values;
	
	public JournalRow() {
		values = new EnumMap<>(JournalField.class);
	}
	
	public void put(JournalField field, String value) {
		values.put(field, value);
		firePropertyChange("row", null, this);
	}
	
	public String get(JournalField field) {
		return values.get(field);
	}
	
	public JournalRow getRow() {
		return this;
	}
}
