package uk.ac.stfc.isis.ibex.configserver.internal;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.Editing;
import uk.ac.stfc.isis.ibex.configserver.configuration.Configuration;
import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.configserver.editing.ObservableEditableConfiguration;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;

public class ConfigEditing extends Closer implements Editing {

	private final ConfigServer configServer;

	public ConfigEditing(ConfigServer configServer) {
		this.configServer = configServer;
	}
	
	@Override
	public InitialiseOnSubscribeObservable<EditableConfiguration> currentConfig() {
		return edit(configServer.currentConfig());
	}

	@Override
	public InitialiseOnSubscribeObservable<EditableConfiguration> blankConfig() {
		return edit(configServer.blankConfig());
	}	
	
	@Override
	public InitialiseOnSubscribeObservable<EditableConfiguration> config(String configName) {
		return edit(configServer.config(configName));
	}

	@Override
	public InitialiseOnSubscribeObservable<EditableConfiguration> component(String componentName) {
		return edit(configServer.component(componentName));
	}
	
	private InitialiseOnSubscribeObservable<EditableConfiguration> edit(ClosableCachingObservable<Configuration> config) {
		return registerForClose(new InitialiseOnSubscribeObservable<>(new ObservableEditableConfiguration(config, configServer)));
	}
}
