package uk.ac.stfc.isis.ibex.ui.dae.run;

import uk.ac.stfc.isis.ibex.dae.DaeRunState;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;

public class InstrumentState extends TransformingObservable<DaeRunState, String> {
	
	
	public InstrumentState(InitialiseOnSubscribeObservable<DaeRunState> runStatus) {
		setSource(runStatus);
	}

	@Override
	protected String transform(DaeRunState value) {
		return value.name();
	}
}