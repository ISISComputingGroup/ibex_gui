package uk.ac.stfc.isis.ibex.dae;

import uk.ac.stfc.isis.ibex.dae.actions.DaeActions;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.ExperimentSetup;
import uk.ac.stfc.isis.ibex.dae.spectra.Spectra;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

public interface IDae {
	InitialiseOnSubscribeObservable<String> instrument();
	
	InitialiseOnSubscribeObservable<String> runNumber();

	InitialiseOnSubscribeObservable<DaeRunState> runState();

	InitialiseOnSubscribeObservable<Boolean> isRunning();
	
	InitialiseOnSubscribeObservable<String> isisCycle();

	InitialiseOnSubscribeObservable<String> title();

	Writable<String> setTitle();
		
	InitialiseOnSubscribeObservable<String> vetos();
		
	ExperimentSetup experimentSetup();

	Spectra spectra();
		
	DaeActions actions();
}
