package uk.ac.stfc.isis.ibex.model;

public class DefaultValue<T> extends UpdatedValue<T> {
		
	public DefaultValue(T value) {
		setValue(value);
	}
}
