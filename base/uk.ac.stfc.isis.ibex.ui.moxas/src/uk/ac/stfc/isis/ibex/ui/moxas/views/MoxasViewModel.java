package uk.ac.stfc.isis.ibex.ui.moxas.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;

/**
 * The Moxa mappings view model.
 */
public class MoxasViewModel extends ModelObject {

	private HashMap<String, MoxaList> moxaPorts = new HashMap<String, MoxaList>();
	private final Configurations control;
	private final UpdatedObservableAdapter<Configuration> currentConfig;
	
	private HashSet<String> expanded = new HashSet<String>(); // Store keys of expanded MoxaLists to re-expand on refresh
	
	/**
	 * Represents all moxa physical port to COM port mappings.
	 * 
	 * @return a map of Moxa port to serial port mappings by Moxa device
	 */
	public HashMap<String, MoxaList> getMoxaPorts() {
		HashMap<String, MoxaList> map = new HashMap<String, MoxaList>();
		Collection<Ioc> iocsInConfig = getIocsInConfig();

		HashMap<String, ArrayList<ArrayList<String>>> ret = control.moxaMappings().getValue();
		if (ret != null) {
			ret.forEach((key, value) -> {
				MoxaList list = new MoxaList(key);
				for (ArrayList<String> item : value) {
					final String comPort = item.get(1);
					
					List<Ioc> iocsUsingComPort = getIocsForCom(iocsInConfig, comPort);
					
					list.add(new MoxaModelObject(item.get(0), comPort, iocsUsingComPort));
				}
				map.put(key, list);
			});
		}
		return map;
	};
	
	private Collection<Ioc> getIocsInConfig() {
		final Collection<Ioc> configIocs = new ArrayList<>();
	
		try {
	    	Configuration currentConfig = control.server().currentConfig().getValue();
	        Collection<Configuration> components = control.server().componentDetails().getValue();
	
	        Set<String> enabledComponentNames =
	                currentConfig.getComponents().stream().map(comp -> comp.getName()).collect(Collectors.toSet());
	
	        configIocs.addAll(currentConfig.getIocs());
	        components.stream().forEach(comp -> {
	            if (enabledComponentNames.contains(comp.getName())) {
	                configIocs.addAll(comp.getIocs());
	            }
	        });
		} catch (Exception ignore) {
			// error while retrieving iocs in config
			// could be that the server is not running
		}
        return configIocs;
	}
	
	public List<Ioc> getIocsForCom(Collection<Ioc> iocs, String comPort) {
		return iocs.stream().filter((ioc) -> {
			Macro portMacro = ioc.getMacros().stream().filter(macro -> macro.getName().equals("PORT")).findFirst().orElse(null);
			if (portMacro == null) return false;
			return portMacro.getValue().equals(comPort);
		}).toList();
	}
	
	private PropertyChangeListener moxasListener;
	
	/**
	 * The constructor of the viewmodel.
	 * 
	 * @param control The configserver singleton instance holding configurations
	 *                info
	 */
	public MoxasViewModel(Configurations control) {
		this.control = control;
		this.currentConfig = new UpdatedObservableAdapter<Configuration>(control.server().currentConfig());
		
		moxaPorts = getMoxaPorts();

		moxasListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				firePropertyChange("moxaMappings", moxaPorts, moxaPorts = getMoxaPorts());
			}
		};

		control.moxaMappings().addPropertyChangeListener(moxasListener);
    	
		// Listen to changes in configuration to update table if e.g. an IOC port is changed
        currentConfig.addPropertyChangeListener(moxasListener);
	}

	/**
	 * Add a currently expanded tree item.
	 * @param key The MoxaList key.
	 */
	public void addExpanded(final String key) {
		expanded.add(key);
	}
	
	/**
	 * Remove from expanded as the tree item was collapsed in the view.
	 * @param key The MoxaList key.
	 */
	public void removeExpanded(final String key) {
		expanded.remove(key);
	}
	
	/**
	 * @return The MoxaLists that were previously expanded.
	 */
	public List<MoxaList> getExpanded() {
		return expanded.stream()
					   .map(key -> moxaPorts.get(key))
					   .collect(Collectors.toList());
	}
	
	/**
	 * Fire the view state restore properties.
	 */
	public void refresh() {
		firePropertyChange("expanded", null, getExpanded());
	}
	
	/**
	 * Removes listeners on Moxa mappings.
	 */
	public void removeListeners() {
		control.moxaMappings().removePropertyChangeListener(moxasListener);
		currentConfig.removePropertyChangeListener(moxasListener);
	}
}
