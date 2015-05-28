package uk.ac.stfc.isis.ibex.ui.dashboard.models;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.dae.DaeRunState;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.dashboard.widgets.RunState;

public class InstrumentState implements Closable {
	
	private final InitialisableObserver<DaeRunState> sourceObserver = new BaseObserver<DaeRunState>() {
		@Override
		public void onValue(DaeRunState value) {
			setState(runState(value));
		}

		@Override
		public void onError(Exception e) {
			setState(RunState.UNKNOWN);
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {
			if (!isConnected) {
				setState(RunState.UNKNOWN);
			}
		}
	};
	
	private final Subscription sourceSubscription;
	private final SettableUpdatedValue<String> text;
	private final SettableUpdatedValue<Color> color;
	
	public InstrumentState(InitialiseOnSubscribeObservable<DaeRunState> source) {
		text = new SettableUpdatedValue<>();
		color = new SettableUpdatedValue<>();
		sourceSubscription = source.subscribe(sourceObserver);
	}

	public UpdatedValue<String> text() {
		return text;
	}
	
	public UpdatedValue<Color> color() {
		return color;
	}
	
	@Override
	public void close() {
		sourceSubscription.cancel();
	}
	
	private void setState(RunState state) {
		text.setValue(state.name());
		color.setValue(state.color());
	}
	
	private static RunState runState(DaeRunState state) {
    	for (RunState runState : RunState.values()) {
    		if (runState.name().equalsIgnoreCase(state.name())) {
    			return runState;
    		}
    	}
    	
        return RunState.UNKNOWN;
	}
}
