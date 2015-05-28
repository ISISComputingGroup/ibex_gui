package uk.ac.stfc.isis.ibex.configserver;

import uk.ac.stfc.isis.ibex.configserver.editing.EditableConfiguration;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;

public interface Editing {

	InitialiseOnSubscribeObservable<EditableConfiguration> currentConfig();
	
	InitialiseOnSubscribeObservable<EditableConfiguration> blankConfig();
	
	InitialiseOnSubscribeObservable<EditableConfiguration> config(String configName);
	
	InitialiseOnSubscribeObservable<EditableConfiguration> component(String componentName);
}
