package uk.ac.stfc.isis.ibex.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class ModelObject implements IModelObject {
	
	private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
		
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    	changeSupport.removePropertyChangeListener(propertyName, listener);
    }
    
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {    	
    	changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }     	    
    
    protected PropertyChangeListener passThrough() {
    	return new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				firePropertyChange(event.getPropertyName(), event.getOldValue(), event.getNewValue());
			}
    	};
    }
    
	protected PropertyChangeListener raiseEventsFor(final String field) {
		return new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				firePropertyChange(field, event.getOldValue(), event.getNewValue());
			}
		};
	}
} 