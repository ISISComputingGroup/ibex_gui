
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Class that allows other objects to be bound to it.
 */
public abstract class ModelObject implements IModelObject {
	
	private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    /**
     * Adds a property change listener.
     * 
     * @param listener
     *            the listener
     */
	@Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

    /**
     * Remove a property change listener.
     * 
     * @param listener
     *            the listener
     */
	@Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

    /**
     * Adds a property change listener.
     * 
     * @param propertyName
     *            the property name
     * @param listener
     *            the listener
     */
	@Override
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

    /**
     * Removes a property change listener.
     * 
     * @param propertyName
     *            the property name
     * @param listener
     *            the listener
     */
    @Override
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
    	changeSupport.removePropertyChangeListener(propertyName, listener);
    }
    
    /**
     * Fires a property change to tell bound objects that a value has changed.
     * 
     * @param propertyName
     *            The property that is changing.
     * @param oldValue
     *            The old value of the property.
     * @param newValue
     *            The new value of the property.
     */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {    
    	changeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }     	    
    
    /**
     * Pass through change listener. Refires the event from the base value.
     *
     * @return the property change listener
     */
    protected PropertyChangeListener passThrough() {
    	return new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				firePropertyChange(event.getPropertyName(), event.getOldValue(), event.getNewValue());
			}
    	};
    }
    
    /**
     * Returns a change listener that refires events from the specified
     * property.
     * 
     * @param field
     *            The property to refire events from.
     * @return The change listener that does the refiring.
     */
	protected PropertyChangeListener raiseEventsFor(final String field) {
		return new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				firePropertyChange(field, event.getOldValue(), event.getNewValue());
			}
		};
	}
} 