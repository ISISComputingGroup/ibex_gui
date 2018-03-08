package uk.ac.stfc.isis.ibex.journal;

import java.util.EnumMap;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Class representing a row in the journal viewer table.
 */
public class JournalRow extends ModelObject {
	private EnumMap<JournalField, String> values;
	
	/**
	 * Construct a new journal row.
	 */
	public JournalRow() {
		values = new EnumMap<>(JournalField.class);
	}
	
	/**
	 * Set a field to a given value.
	 * @param field the field to set
	 * @param value the value to set
	 */
	public void put(JournalField field, String value) {
		values.put(field, value);
		firePropertyChange("row", null, this);
	}
	
	/**
	 * Gets the value of a field.
	 * @param field the field to get the value of
	 * @return the value
	 */
	public String get(JournalField field) {
		return values.get(field);
	}
	
	/**
	 * Gets the row.
	 * @return this
	 */
	public JournalRow getRow() {
		return this;
	}
}
