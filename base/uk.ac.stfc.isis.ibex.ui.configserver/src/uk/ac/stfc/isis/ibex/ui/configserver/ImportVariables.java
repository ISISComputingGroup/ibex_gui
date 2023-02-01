/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2023 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.ui.configserver;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.internal.Converters;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentUtils;
import uk.ac.stfc.isis.ibex.instrument.channels.CompressedCharWaveformChannel;
import uk.ac.stfc.isis.ibex.instrument.channels.StringChannel;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The import variables. Contains observables from a remote instrument.
 */
public class ImportVariables extends ModelObject {
	
	private static final String VERSION_ADDRESS = "CS:VERSION:SVN:REV";
	private static final String COMPONENTS_ADDRESS = "CS:BLOCKSERVER:ALL_COMPONENT_DETAILS";
	
	private final Converters converters;
	private String localPrefix;
    private String remotePrefix;
	
	private ClosableObservable<String> localConfigVersionObservable;
	private ClosableObservable<String> remoteConfigVersionObservable;
    private ForwardingObservable<Collection<Configuration>> componentsObservable;

    private boolean connected = false;
    private boolean versionMatch = false;
    private Collection<Configuration> components = null;
   
    /**
     * Constructor.
     * 
     * @param converters
     */
	public ImportVariables(Converters converters) {
		this.converters = converters;
		this.localPrefix = Instrument.getInstance().getPvPrefix();
		localConfigVersionObservable = new StringChannel().reader(localPrefix.concat(VERSION_ADDRESS));
	}
	
	/**
	 * Select the remote instrument to observe.
	 * 
	 * @param remotePrefix The remote instrument PV prefix.
	 */
	public void selectInstrument(String remotePrefix) {
		if (remoteConfigVersionObservable != null) {
			remoteConfigVersionObservable.close();
		}
		if (componentsObservable != null) {
			componentsObservable.close();
		}
		
		this.remotePrefix = remotePrefix;
		
		remoteConfigVersionObservable = new StringChannel().reader(remotePrefix.concat(VERSION_ADDRESS));
		remoteConfigVersionObservable.subscribe(new BaseObserver<String>() {
			@Override
		    public void onValue(String value) {
				var localVersion = localConfigVersionObservable.getValue();
				// Always allow copying if either block server is on a development version.
				var match = value.equals(localVersion) || value.startsWith("0.0.0") || localVersion.startsWith("0.0.0");
				firePropertyChange("status", versionMatch, versionMatch = match);
		    }
		});
		
		componentsObservable = InstrumentUtils.convert(new CompressedCharWaveformChannel().reader(remotePrefix.concat(COMPONENTS_ADDRESS)), converters.toConfigList());
		componentsObservable.subscribe(new BaseObserver<Collection<Configuration>>() {
			@Override
		    public void onValue(Collection<Configuration> value) {
				firePropertyChange("components", components, components = value);
		    }
			
			@Override
		    public void onConnectionStatus(boolean isConnected) {
				firePropertyChange("status", null, connected = isConnected);
				
				if (!isConnected) {
					firePropertyChange("components", components, components = null);
				}
		    }
		});
	}
	
	/**
	 * @return The remote instrument prefix.
	 */
	public String getRemotePrefix() {
		return remotePrefix;
	}
	
	/**
	 * @return If the remote instrument Block Server is running.
	 */
	public boolean connected() {
		return connected;
	}
	
	/**
	 * @return If the remote instrument and local instrument are on the same configuration version.
	 */
	public boolean versionMatch() {
		return versionMatch;
	}
	
	/**
	 * @return The remote instrument components.
	 */
	public Collection<Configuration> getComponents() {
		return components;
	}
}
