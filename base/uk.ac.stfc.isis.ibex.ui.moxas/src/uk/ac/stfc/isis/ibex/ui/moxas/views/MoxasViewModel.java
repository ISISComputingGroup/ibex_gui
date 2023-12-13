/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2023
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any kind,
 * either expressed or implied, including but not limited to the implied
 * warranties of merchantability and/or fitness for a particular purpose.
 */

package uk.ac.stfc.isis.ibex.ui.moxas.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.Ioc;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The Moxa mappings view model.
 */
public class MoxasViewModel extends ModelObject {

	private HashMap<String, MoxaList> moxaPorts = new HashMap<String, MoxaList>();
	private final Configurations control;
	private final UpdatedObservableAdapter<Configuration> currentConfig;
	private static final String MIB_DELIM = "::";
	private static final String DELIM = "~";
	private static final String EQUALS = "=";
	private static final String OPER_STATUS = "ifOperStatus";
	
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
					final String[] info = item.get(1).split(DELIM);
					final String comPort = info[0];
					final String status = getStatus(info);
					final String additionalInfo = getAdditionalInfo(info);
					List<Ioc> iocsUsingComPort = getIocsForCom(iocsInConfig, comPort);
					
					list.add(new MoxaModelObject(item.get(0), comPort, iocsUsingComPort, status, additionalInfo));
				}
				map.put(key, list);
			});
		}
		return map;
	}

	/**
	 * @param info the status information available for the port
	 * @return formatted status information
	 */
	private String getStatus(final String[] info) {
		String status = "";
		if (info.length > 1) {
			for  (String infoData : info) {		
				if (infoData.contains(MIB_DELIM) && infoData.toUpperCase().contains(OPER_STATUS.toUpperCase())) {
					status = infoData.split(EQUALS)[1].toUpperCase();
					break;
				}
			}
		}
		return status;
	}

	/**
	 * @param info the additional information available for the port
	 * @return formatted additional information
	 */
	private String getAdditionalInfo(final String[] info) {
		String additionalInfo = "";
		if (info.length > 1) {
			for  (String infoData : info) {
				if (infoData.contains(MIB_DELIM) && !infoData.toUpperCase().contains(OPER_STATUS.toUpperCase())) {
					final String[] dataPoints = infoData.split(MIB_DELIM);
					additionalInfo += String.format("%-20s", dataPoints[1].substring(2));
				}			
			}
		}
		return additionalInfo;
	}
	
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
	
	/**
	 * Get a list of IOCs for a given COM port.
	 * @param iocs All IOCs in the current config.
	 * @param comPort a COM port to check against.
	 * @return A list of IOCs for a given COM port.
	 */
	public List<Ioc> getIocsForCom(Collection<Ioc> iocs, String comPort) {
		return iocs.stream().filter((ioc) -> {
			Macro portMacro = ioc.getMacros().stream().filter(macro -> macro.getName().equals("PORT")).findFirst().orElse(null);
			if (portMacro == null) {
				return false; 
			}
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
	
    /**
     * Method returns a formatted string representing the duration in days and other units.
     * @param time - the duration in milliseconds
     * @return the formatted duration
     */
	public static String toDaysHoursMinutes(Long time) {
		final Duration duration = Duration.ofMillis(time);
		final long days = duration.toDaysPart();
		String upTime;
		if (2 < days) {
			upTime = String.format("%d Day(s)", days);
		} else {
			upTime = String.format("%d Day(s) %d Hour(s) %d Minute(s) %d Seconds(s)", duration.toDaysPart(),
					duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart());
		}

		return upTime;
	}
}
