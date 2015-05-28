package uk.ac.stfc.isis.ibex.ui.dashboard.models;

import uk.ac.stfc.isis.ibex.dashboard.DashboardObservables;
import uk.ac.stfc.isis.ibex.epics.adapters.TextUpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.ObservablePair;
import uk.ac.stfc.isis.ibex.epics.observing.Pair;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class MonitorPanelModel extends Closer {

	private final UpdatedValue<String> goodOverRawFrames;
	private final UpdatedValue<String> currentOverTotal;
	private final UpdatedValue<String> monitorCounts;
	
	public MonitorPanelModel(DashboardObservables observables) {
		goodOverRawFrames = createGoodOverRawFrames(observables);
		currentOverTotal = createCurrentOverTotal(observables);
		monitorCounts = createMonitorCounts(observables);
	}
	
	public UpdatedValue<String> goodOverRawFrames() {
		return goodOverRawFrames;
	}
	
	public UpdatedValue<String> monitorCounts() {
		return monitorCounts;
	}

	public UpdatedValue<String> currentOverTotal() {
		return currentOverTotal;
	}

	private UpdatedValue<String> createGoodOverRawFrames(DashboardObservables observables) {
		ClosableCachingObservable<Pair<Integer, Integer>> pair = registerForClose(new ObservablePair<>(observables.dae.goodFrames, observables.dae.rawFrames));
		InitialiseOnSubscribeObservable<String> ratio = new InitialiseOnSubscribeObservable<String>(new ObservableSimpleRatio<>(pair));
		
		return registerForClose(new TextUpdatedObservableAdapter(ratio));
	}
	
	private UpdatedValue<String> createCurrentOverTotal(DashboardObservables observables) {
		ClosableCachingObservable<Pair<Number, Number>> pair = registerForClose(new ObservablePair<>(observables.dae.beamCurrent, observables.dae.goodCurrent));
		InitialiseOnSubscribeObservable<String> ratio = new InitialiseOnSubscribeObservable<String>(new ObservableDecimalRatio(pair));
		
		return registerForClose(new TextUpdatedObservableAdapter(ratio));
	}

	private UpdatedValue<String> createMonitorCounts(DashboardObservables observables) {
		TransformingObservable<Integer, String> monitorCountsAsString = new TransformingObservable<Integer, String>() {
			@Override
			protected String transform(Integer value) {
				return value.toString();
			}
		};
		monitorCountsAsString.setSource(observables.dae.monitorCounts);
		
		return registerForClose(new TextUpdatedObservableAdapter(new InitialiseOnSubscribeObservable<>(monitorCountsAsString)));
	}
}
