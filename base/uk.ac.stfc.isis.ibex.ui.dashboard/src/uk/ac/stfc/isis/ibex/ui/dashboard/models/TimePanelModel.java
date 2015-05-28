package uk.ac.stfc.isis.ibex.ui.dashboard.models;

import uk.ac.stfc.isis.ibex.dashboard.DashboardObservables;
import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ObservablePair;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class TimePanelModel extends Closer {

	private final UpdatedValue<String> instrumentTime;
	private final UpdatedValue<String> runTime;
	private final UpdatedValue<String> period;

	public TimePanelModel(DashboardObservables observables) {
		instrumentTime = registerForClose(new TextUpdatedObservableAdapter(observables.instrumentTime));
		runTime = registerForClose(new TextUpdatedObservableAdapter(observables.dae.runTime));
		period = createPeriod(observables);
	}
	
	public UpdatedValue<String> instrumentTime() {
		return instrumentTime;
	}
	
	public UpdatedValue<String> runTime() {
		return runTime;
	}
	
	public UpdatedValue<String> period() {
		return period;
	}
	
	private UpdatedValue<String> createPeriod(DashboardObservables observables) {
		return registerForClose(
				new TextUpdatedObservableAdapter(
						new InitialiseOnSubscribeObservable<String>(
								new ObservableSimpleRatio<>(
										new ObservablePair<>(observables.dae.currentPeriod, observables.dae.totalPeriods)))));
	}
}
