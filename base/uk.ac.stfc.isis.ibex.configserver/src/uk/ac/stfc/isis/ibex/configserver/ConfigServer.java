
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

package uk.ac.stfc.isis.ibex.configserver;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.configuration.ComponentInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.configserver.internal.FilteredIocs;
import uk.ac.stfc.isis.ibex.epics.conversion.DoNothingConverter;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.writing.LoggingForwardingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.epics.writing.WritingSetCommand;
import uk.ac.stfc.isis.ibex.model.SetCommand;

/**
 * A class that exposes the information contained within configurations on the blockserver.
 *
 */
public class ConfigServer extends Closer {

	private final ConfigServerVariables variables;
	
	/**
	 * The default constructor for the ConfigServer.
	 * @param variables The class that holds all the observables to the various blockserver PVs.
	 */
	public ConfigServer(ConfigServerVariables variables) {
		this.variables = variables;
	}

	/**
	 * Returns an observable conveying the value of a given block.
	 * @param blockName the name of the block.
	 * @return the String observable object
	 */
	public ForwardingObservable<String> blockValue(String blockName) {
		return variables.blockValue(blockName);
	}
	
	/**
	 * Returns an observable conveying the description of a given block.
	 * @param blockName the name of the block.
	 * @return the String observable object
	 */
	public ForwardingObservable<String> blockDescription(String blockName) {
		return variables.blockDescription(blockName);
	}

    /**
     * Returns an observable conveying the alarm state of a given block.
     * 
     * @param blockName the name of the block
     * @return the {@link AlarmState} observable object
     */
    public ForwardingObservable<AlarmState> alarm(String blockName) {
        return variables.alarm(blockName);
    }

	/**
	 * Returns the PV that the blockserver exposes for a given block.
	 * @param blockName the name of the block.
	 * @return the name of the PV relating to the block in the BlockServer
	 */
	public String blockServerAlias(String blockName) {
		return variables.blockServerAlias(blockName);
	}
	
	/**
	 * Returns an observable on the status of the blockserver.
	 * @return the {@link ServerStatus} observable object
	 */
	public ForwardingObservable<ServerStatus> serverStatus() {
		return variables.serverStatus;
	}

	/**
	 * Returns an observable to the current configuration loaded on the instrument.
	 * @return the {@link Configuration} observable object
	 */
	public ForwardingObservable<Configuration> currentConfig() {
		return variables.currentConfig;
	}

	/**
	 * Returns an observable to a blank configuration to be used when first creating a configuration.
	 * @return the {@link Configuration} observable object
	 */
	public ForwardingObservable<Configuration> blankConfig() {
		return variables.blankConfig;
	}

	/**
	 * Returns an observable to the given configuration.
	 * @param configName the name of the config
	 * @return the {@link Configuration} observable object
	 */
	public ForwardingObservable<Configuration> config(String configName) {
		return variables.config(configName);
	}

	/**
	 * Returns an observable to the information on what configurations are available.
	 * @return the Collection<{@link ConfigInfo}> observable object
	 */
	public ForwardingObservable<Collection<ConfigInfo>> configsInfo() {
		return variables.configsInfo;
	}

	/**
	 * Returns an observable to the information on what components are available.
	 * @return the Collection<{@link ConfigInfo}> observable object
	 */
	public ForwardingObservable<Collection<ConfigInfo>> componentsInfo() {
		return variables.componentsInfo;
	}

    /**
     * Returns an observable to the details of all available components.
     * 
     * @return the Collection<{@link Configuration}> observable object
     */
    public ForwardingObservable<Collection<Configuration>> componentDetails() {
        return variables.componentDetails;
    }

	/**
	 * Returns an observable to the iocs that are available on the instrument.
	 * @return the Collection<{@link EditableIoc}> observable object
	 */
	public ForwardingObservable<Collection<EditableIoc>> iocs() {
		return variables.iocs;
	}
	
	/**
	 * Returns an observable to all the pvs that have been available on the instrument.
	 * @return the Collection<{@link PV}> observable object
	 */
	public ForwardingObservable<Collection<PV>> pvs() {
		return variables.pvs;
	}
	
	/**
	 * Returns an observable to the PVs that are currently available on the instrument.
	 * @return the Collection<{@link PV}> observable object
	 */
	public ForwardingObservable<Collection<PV>> activePVs() {
		return variables.activePVs;
	}

	/**
	 * Returns an observable to the components that are currently available on the instrument.
	 * @return the Collection<{@link ComponentInfo}> observable object
	 */
	public ForwardingObservable<Collection<ComponentInfo>> components() {
		return variables.components;
	}

	/**
	 * Returns an observable to the information contained in the given component.
	 * @param componentName the name of the component
	 * @return the {@link Configuration} observable object
	 */
	public ForwardingObservable<Configuration> component(String componentName) {
		return variables.component(componentName);
	}
	
	/**
	 * Returns a writable to set the current configuration on the instrument.<br>
	 * The writable expects a Configuration  with the name of the configuration to load.
	 * @return the {@link Configuration} writable object
	 */
	public Writable<Configuration> setCurrentConfig() {
		return variables.setCurrentConfiguration;
	}

	/**
	 * Returns a writable to load a new configuration.<br>
	 * The writable expects a string with the name of the configuration to load.
	 * @return the String writable object
	 */
	public Writable<String> load() {
		return variables.loadConfiguration;
	}

	/**
	 * Returns a writable to save a configuration.<br>
	 * The writable expects a {@link Configuration} to save.
	 * @return the {@link Configuration} writable object
	 */
	public Writable<Configuration> saveAs() {
		return variables.saveAsConfiguration;
	}

	/**
	 * Returns a writable to save a configuration as a component.<br>
	 * The writable expects a {@link Configuration} to save as a component.
	 * @return the {@link Configuration} writable object
	 */
	public Writable<Configuration> saveAsComponent() {
		return variables.saveAsComponent;
	}

	/**
	 * Returns a writable to delete a number of configurations.<br>
	 * The writable expects a {@code Collection<String>} with names of the configurations to delete.
	 * @return the {@code Collection<String>} writable object
	 */
	public Writable<Collection<String>> deleteConfigs() {
		return variables.deleteConfigurations;
	}

	/**
	 * Returns a writable to delete a number of components.<br>
	 * The writable expects a {@code Collection<String>} with names of the components to delete.
	 * @return the {@code Collection<String>} writable object
	 */
	public Writable<Collection<String>> deleteComponents() {
		return variables.deleteComponents;
	}
	
	/**
	 * Returns a collection of the names of all the configurations on the instrument.
	 * @return the {@code Collection<String>} of configuration names
	 */
	public Collection<String> configNames() {
		return ConfigInfo.names(configsInfo().getValue());
	}
	
	/**
	 * Returns a collection of the names of all the components on the instrument.
	 * @return the {@code Collection<String>} of configuration names
	 */
	public Collection<String> componentNames() {
		return ConfigInfo.names(componentsInfo().getValue());
	}
	
	/**
	 * Returns an observable to the states of all of the iocs.
	 * @return the Collection<{@link EditableIocState}> observable object
	 */
    public ForwardingObservable<Collection<IocState>> iocStates() {
        ForwardingObservable<Collection<IocState>> iocs = 
                new ForwardingObservable<>(variables.iocStates);
		
		return new ForwardingObservable<>(new FilteredIocs(iocs, variables.protectedIocs));
	}
	
	/**
	 * Returns a {@link SetCommand} object that can be used to start any ioc. <br>
	 * The SetCommand expects a collection of the names of the iocs to start.
	 * @return the SetCommand for starting a number of iocs
	 */
	public SetCommand<Collection<String>> startIoc() {
		return WritingSetCommand.forDestination(log("Start ioc", variables.startIoc));		
	}

	/**
	 * Returns a {@link SetCommand} object that can be used to stop any ioc. <br>
	 * The SetCommand expects a collection of the names of the iocs to stop.
	 * @return the SetCommand for stopping a number of iocs
	 */
	public SetCommand<Collection<String>> stopIoc() {
		return WritingSetCommand.forDestination(log("Stop ioc", variables.stopIoc));		
	}
	
	/**
	 * Returns a {@link SetCommand} object that can be used to restart any ioc. <br>
	 * The SetCommand expects a collection of the names of the iocs to start.
	 * @return the SetCommand for restarting a number of iocs
	 */
	public SetCommand<Collection<String>> restartIoc() {
		return WritingSetCommand.forDestination(log("Restart ioc", variables.restartIoc));		
	}

    /**
     * Provides a writable that logs when it is written to.
     * 
     * @param <T> This is the type parameter
     * @param id the message to write
     * @param destination the writable being written to
     * @return the logging writable
     */
	private <T> Writable<T> log(String id, Writable<T> destination) {
        return new LoggingForwardingWritable<>(Configurations.LOG, id, destination, new DoNothingConverter<T>());
	}
}
