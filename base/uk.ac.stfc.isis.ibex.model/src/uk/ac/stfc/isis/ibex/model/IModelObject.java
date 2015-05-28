package uk.ac.stfc.isis.ibex.model;

import java.beans.PropertyChangeListener;

public interface IModelObject {
	void addPropertyChangeListener(PropertyChangeListener listener);

	void removePropertyChangeListener(PropertyChangeListener listener);

	void addPropertyChangeListener(String propertyName, PropertyChangeListener listener);

    void removePropertyChangeListener(String propertyName, PropertyChangeListener listener); 
}
