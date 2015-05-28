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
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.epics.writing.ConfigurableWriter;
import uk.ac.stfc.isis.ibex.epics.writing.LoggingForwardingWritable;
import uk.ac.stfc.isis.ibex.epics.writing.LoggingForwardingWriter;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.epics.writing.Writer;
import uk.ac.stfc.isis.ibex.epics.writing.Writers;
import uk.ac.stfc.isis.ibex.epics.writing.WritingSetCommand;
import uk.ac.stfc.isis.ibex.model.SetCommand;

public class ConfigServer extends Closer {

	private final ConfigServerVariables variables;
	
	public ConfigServer(ConfigServerVariables variables) {
		this.variables = variables;
	}

	public InitialiseOnSubscribeObservable<String> blockValue(String blockName) {
		return variables.blockValue(blockName);
	}
	
	public InitialiseOnSubscribeObservable<String> blockDescription(String blockName) {
		return variables.blockDescription(blockName);
	}
	
	public String blockServerAlias(String blockName) {
		return variables.blockServerAlias(blockName);
	}
	
	public InitialiseOnSubscribeObservable<ServerStatus> serverStatus() {
		return variables.serverStatus;
	}

	public InitialiseOnSubscribeObservable<Configuration> currentConfig() {
		return variables.currentConfig;
	}

	public InitialiseOnSubscribeObservable<Configuration> blankConfig() {
		return variables.blankConfig;
	}

	public InitialiseOnSubscribeObservable<Configuration> config(String configName) {
		return variables.config(configName);
	}

	public InitialiseOnSubscribeObservable<Collection<ConfigInfo>> configsInfo() {
		return variables.configsInfo;
	}

	public InitialiseOnSubscribeObservable<Collection<ConfigInfo>> componentsInfo() {
		return variables.componentsInfo;
	}

	public InitialiseOnSubscribeObservable<Collection<EditableIoc>> iocs() {
		return variables.iocs;
	}

	public InitialiseOnSubscribeObservable<String> iocDescription(String iocName) {
		return variables.iocDescription(iocName);
	}

	public Writer<String> iocDescriptionSetter(String name) {
		return registerForClose(Writers.forDestination(variables.iocDescriptionSetter(name)));
	}
	
	public InitialiseOnSubscribeObservable<Collection<PV>> pvs() {
		return variables.pvs;
	}
	
	public InitialiseOnSubscribeObservable<Collection<PV>> active_pvs() {
		return variables.active_pvs;
	}

	public InitialiseOnSubscribeObservable<Collection<Component>> components() {
		return variables.components;
	}

	public InitialiseOnSubscribeObservable<Configuration> component(String componentName) {
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
		return ConfigInfo.names(configsInfo().value());
	}
	
	public Collection<String> componentNames() {
		return ConfigInfo.names(componentsInfo().value());
	}
	
	public InitialiseOnSubscribeObservable<Collection<EditableIocState>> iocStates() {
		InitialiseOnSubscribeObservable<Collection<EditableIocState>> iocs = 
				new InitialiseOnSubscribeObservable<>(
						new ConvertingObservable<>(variables.iocStates, new IocStateEditingConverter(this)));
		
		return new InitialiseOnSubscribeObservable<>(new FilteredIocs(iocs, variables.protectedIocs));
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
