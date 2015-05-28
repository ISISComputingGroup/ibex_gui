package uk.ac.stfc.isis.ibex.model;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class SettableUpdatedValue<T> extends UpdatedValue<T> {
	
	public SettableUpdatedValue() {
	}
	
	public SettableUpdatedValue(T value) {
		setValue(value);
	}
	
	public void setValue(T value) {
		super.setValue(value);
	}
}
