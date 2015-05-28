package uk.ac.stfc.isis.ibex.ui.configserver.editing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class ConfigSelection extends ModelObject {

	private final Map<String, Configuration> available = new LinkedHashMap<>();
	private Configuration selected;
	
	public ConfigSelection(final UpdatedValue<Collection<Configuration>> availableConfigs) {
		updateConfigs(availableConfigs.getValue());
		availableConfigs.addPropertyChangeListener(new PropertyChangeListener() {		
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				updateConfigs(availableConfigs.getValue());
			}
		});
	}
	
	public Configuration getSelected() {
		return selected;
	}
	
	public void setSelected(String config) {
		if (!available.containsKey(config)) {
			return;
		}
		
		Configuration newSelection = available.get(config);
		if (newSelection.equals(config)) {
			return;
		}
		
		firePropertyChange("selected", selected, selected = newSelection);
	}
	
	public Collection<String> availableConfigs() {
		return available.keySet();
	}
	
	private void updateConfigs(Collection<Configuration> availableConfigs) {
		Collection<String> availableBeforeUpdate = new ArrayList<>(available.keySet());
		available.clear();
		for (Configuration config : safe(availableConfigs)) {
			available.put(config.name(), config);
		}
		
		firePropertyChange("available", availableBeforeUpdate, availableConfigs());
	}
	
	private static <T> Collection<T> safe(Collection<T> other ) {
	    return other == null ? Collections.<T>emptyList() : other;
	} 
}
