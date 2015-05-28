package uk.ac.stfc.isis.ibex.dae.actions;

import uk.ac.stfc.isis.ibex.dae.DaeObservables;
import uk.ac.stfc.isis.ibex.dae.DaeWritables;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.Action;

public class DaeActions extends Closer {

	public final Action begin;
	public final Action end;
	public final Action pause;
	public final Action resume;
	public final Action abort;
	public final Action cancelAbort;
	public final Action save;
	
	public DaeActions(DaeWritables targets, DaeObservables observables){
		begin = registerForClose(new Begin(targets.begin, observables.inStateTransition, observables.runState));
		end = registerForClose(new End(targets.end, observables.inStateTransition, observables.runState));
		pause = registerForClose(new Pause(targets.pause, observables.inStateTransition, observables.runState));
		resume = registerForClose(new Resume(targets.resume, observables.inStateTransition, observables.runState));
		abort = registerForClose(new Abort(targets.abort, observables.inStateTransition, observables.runState));
		cancelAbort = registerForClose(new CancelAbort(targets.recover, observables.inStateTransition, observables.runState));
		save = registerForClose(new Save(targets.save, observables.inStateTransition, observables.runState));
	}
}
