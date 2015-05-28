package uk.ac.stfc.isis.ibex.dae.actions;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.dae.DaeRunState;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

public final class Begin extends DaeAction {

	private static final Collection<DaeRunState> ALLOWED = new ArrayList<>();
	static {
		ALLOWED.add(DaeRunState.SETUP);
	}
	
	public Begin(Writable<String> target,
			InitialiseOnSubscribeObservable<Boolean> inStateTransition,
			InitialiseOnSubscribeObservable<DaeRunState> runState) {
		super(target, inStateTransition, runState);
	}

	@Override
	protected boolean allowed(DaeRunState runState) {
		return ALLOWED.contains(runState);
	}
}
