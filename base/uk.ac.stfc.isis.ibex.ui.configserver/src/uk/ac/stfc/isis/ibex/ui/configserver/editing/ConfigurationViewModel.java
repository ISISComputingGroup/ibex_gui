package uk.ac.stfc.isis.ibex.ui.configserver.editing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class ConfigurationViewModel extends ModelObject {
	
	private final UpdatedValue<Configuration> config;

	private final Map<String, Ioc> iocs = new HashMap<>();
	private UpdatedValue<Collection<Ioc>> knownIocs;
	
	private final PropertyChangeListener updateIocs = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			updateIocs();
		}
	};
	
	public ConfigurationViewModel(UpdatedValue<Configuration> config, UpdatedValue<Collection<Ioc>> knownIocs) {
		this.config = config;
		this.knownIocs = knownIocs;
		
		knownIocs.addPropertyChangeListener(updateIocs);
		config.addPropertyChangeListener(updateIocs);
		updateIocs();
	}
	
	public Collection<Ioc> iocs() {
		return asSortedList(iocs.values());
	}
	
	private static <T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
		List<T> list = new ArrayList<T>(c);
		java.util.Collections.sort(list);
		  
		return list;
	}
	
	private synchronized void updateIocs() {
		Collection<Ioc> iocsBeforeUpdate = iocs();
		
		iocs.clear();
		addIocs(knownIocs.getValue());
		Configuration configValue = config.getValue();
		if (configValue != null) {	
			addIocs(configValue.getIocs());
		}
		
		firePropertyChange("iocs", iocsBeforeUpdate, iocs());
	}
	
	private void addIocs(Collection<Ioc> iocsToAdd) {
		if (iocsToAdd == null) {
			return;
		}
		
		for (Ioc ioc : iocsToAdd) {
			iocs.put(ioc.getName(), ioc);
		}
	}	
}
