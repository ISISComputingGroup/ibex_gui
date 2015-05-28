package uk.ac.stfc.isis.ibex.dae;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;

public class DaeIsRunning extends TransformingObservable<DaeRunState, Boolean> {

	public DaeIsRunning(ClosableCachingObservable<DaeRunState> source) {
		setSource(source);
	}

	@Override
	protected Boolean transform(DaeRunState value) {
		return value.equals(DaeRunState.RUNNING);
	}

}
