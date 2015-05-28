package uk.ac.stfc.isis.ibex.model;

public interface ReadableValue<T> extends IModelObject {
	/*
	 * The value, may be null
	 */
	public T getValue();
}
