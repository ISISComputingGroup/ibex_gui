package uk.ac.stfc.isis.ibex.configserver;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayConfiguration;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;

public interface Displaying {
	/*
	 * Configuration details for presentation to the user.
	 */
	InitialiseOnSubscribeObservable<DisplayConfiguration> displayCurrentConfig();
}
