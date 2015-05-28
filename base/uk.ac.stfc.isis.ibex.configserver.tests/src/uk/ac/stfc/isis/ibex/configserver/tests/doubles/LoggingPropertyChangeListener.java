package uk.ac.stfc.isis.ibex.configserver.tests.doubles;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoggingPropertyChangeListener implements PropertyChangeListener {

	private PropertyChangeEvent lastEvent;
	
	public PropertyChangeEvent lastEvent() {
		return lastEvent;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		lastEvent = event;
	}

}
