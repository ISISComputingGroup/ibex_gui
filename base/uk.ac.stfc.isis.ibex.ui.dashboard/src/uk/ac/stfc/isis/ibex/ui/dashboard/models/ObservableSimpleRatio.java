package uk.ac.stfc.isis.ibex.ui.dashboard.models;

import uk.ac.stfc.isis.ibex.epics.observing.ClosableCachingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Pair;
import uk.ac.stfc.isis.ibex.epics.observing.TransformingObservable;

public class ObservableSimpleRatio<T1, T2> extends TransformingObservable<Pair<T1, T2>, String> {

	public ObservableSimpleRatio(ClosableCachingObservable<Pair<T1, T2>> source) {
		setSource(source);
	}
	
	@Override
	protected String transform(Pair<T1, T2> value) {		
		if (value.first == null || value.second == null) {
			return "Unknown";
		}
		
		return value.first + " / " + value.second;
	}
}
