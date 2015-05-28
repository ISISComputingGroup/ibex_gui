package uk.ac.stfc.isis.ibex.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @param <T> Type of the value provided
 */
public class UpdatedValue<T> extends ModelObject {

	private T value;
	private boolean isSet;
	
	/*
	 * (non-Javadoc)
	 * @see uk.ac.stfc.isis.ibex.model.ReadableValue#getValue()
	 */
	public synchronized final T getValue() {
		return value;
	}
	
	public synchronized boolean isSet() {
		return isSet;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener, boolean executeIfAlreadySet) {
		super.addPropertyChangeListener(listener);

		if (executeIfAlreadySet && isSet) {
			listener.propertyChange(new PropertyChangeEvent(this, "value", value, value));
		}
	}
	
	protected synchronized void setValue(T value) {
		isSet = true;
		firePropertyChange("value", this.value, this.value = value);
	}
}
