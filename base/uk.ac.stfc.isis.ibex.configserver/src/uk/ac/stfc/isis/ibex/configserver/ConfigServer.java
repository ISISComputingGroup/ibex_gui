
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

import uk.ac.stfc.isis.ibex.configserver.configuration.Component;
import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.configuration.PV;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableIoc;
import uk.ac.stfc.isis.ibex.configserver.internal.FilteredIocs;
import uk.ac.stfc.isis.ibex.configserver.internal.IocStateEditingConverter;
import uk.ac.stfc.isis.ibex.epics.observing.ConvertingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.writing.ConfigurableWriter;
import uk.ac.stfc.isis.ibex.epics.writing.LoggingForwardingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.LoggingForwardingWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.epics.writing.Writer;
import uk.ac.stfc.isis.ibex.epics.writing.ClosableSameTypeWriter;
import uk.ac.stfc.isis.ibex.epics.writing.WritingSetCommand;
import uk.ac.stfc.isis.ibex.model.SetCommand;

public class ConfigServer extends Closer {

	private final ConfigServerVariables variables;
	
	public ConfigServer(ConfigServerVariables variables) {
		this.variables = variables;
	}

	public ForwardingObservable<String> blockValue(String blockName) {
		return variables.blockValue(blockName);
	}
	
	public ForwardingObservable<String> blockDescription(String blockName) {
		return variables.blockDescription(blockName);
	}
	
	public String blockServerAlias(String blockName) {
		return variables.blockServerAlias(blockName);
	}
	
	public ForwardingObservable<ServerStatus> serverStatus() {
		return variables.serverStatus;
	}

	public ForwardingObservable<Configuration> currentConfig() {
		return variables.currentConfig;
	}

	public ForwardingObservable<Configuration> blankConfig() {
		return variables.blankConfig;
	}

	public ForwardingObservable<Configuration> config(String configName) {
		return variables.config(configName);
	}

	public ForwardingObservable<Collection<ConfigInfo>> configsInfo() {
		return variables.configsInfo;
	}

	public ForwardingObservable<Collection<ConfigInfo>> componentsInfo() {
		return variables.componentsInfo;
	}

	public ForwardingObservable<Collection<EditableIoc>> iocs() {
		return variables.iocs;
	}

	public ForwardingObservable<String> iocDescription(String iocName) {
		return variables.iocDescription(iocName);
	}

	public Writer<String> iocDescriptionSetter(String name) {
		return registerForClose(ClosableSameTypeWriter.newInstance(variables.iocDescriptionSetter(name)));
	}
	
	public ForwardingObservable<Collection<PV>> pvs() {
		return variables.pvs;
	}
	
	public ForwardingObservable<Collection<PV>> activePVs() {
		return variables.activePVs;
	}

	public ForwardingObservable<Collection<Component>> components() {
		return variables.components;
	}

	public ForwardingObservable<Configuration> component(String componentName) {
		return variables.component(componentName);
	}
	
	public Writable<Configuration> setCurrentConfig() {
		return variables.setCurrentConfiguration;
	}

	public Writable<String> load() {
		return variables.loadConfiguration;
	}

	public Writable<String> save() {
		return variables.saveConfiguration;
	}

	public Writable<Configuration> saveAs() {
		return variables.saveAsConfiguration;
	}

	public Writable<Configuration> saveAsComponent() {
		return variables.saveAsComponent;
	}

	public Writable<Collection<String>> deleteConfigs() {
		return variables.deleteConfigurations;
	}

	public Writable<Collection<String>> deleteComponents() {
		return variables.deleteComponents;
	}
	
	public Collection<String> configNames() {
		return ConfigInfo.names(configsInfo().getValue());
	}
	
	public Collection<String> componentNames() {
		return ConfigInfo.names(componentsInfo().getValue());
	}
	
	public ForwardingObservable<Collection<EditableIocState>> iocStates() {
		ForwardingObservable<Collection<EditableIocState>> iocs = 
				new ForwardingObservable<>(
						new ConvertingObservable<>(variables.iocStates, new IocStateEditingConverter(this)));
		
		return new ForwardingObservable<>(new FilteredIocs(iocs, variables.protectedIocs));
	}
	
	public SetCommand<Collection<String>> startIoc() {
		return WritingSetCommand.forDestination(log("Start ioc", variables.startIoc));		
	}

	public SetCommand<Collection<String>> stopIoc() {
		return WritingSetCommand.forDestination(log("Stop ioc", variables.stopIoc));		
	}
	
	public SetCommand<Collection<String>> restartIoc() {
		return WritingSetCommand.forDestination(log("Restart ioc", variables.restartIoc));		
	}

	public <T> Writable<T> log(String id, Writable<T> destination) {
		return new LoggingForwardingWritable<>(Configurations.LOG, id, destination);
	}
	
	public <T> Writer<T> log(String id, ConfigurableWriter<T, T> writer) {
		return registerForClose(new LoggingForwardingWriter<>(Configurations.LOG, id, writer));
	}
}
