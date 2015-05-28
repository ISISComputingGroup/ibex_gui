package uk.ac.stfc.isis.ibex.ui.dashboard.models;

import uk.ac.stfc.isis.ibex.dashboard.ShutterStatus;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class ShutterState implements Closable {
	
	private final InitialisableObserver<ShutterStatus> sourceObserver = new BaseObserver<ShutterStatus>() {
		@Override
		public void onValue(ShutterStatus value) {
			setState(value);
		}

		@Override
		public void onError(Exception e) {
			setState(ShutterStatus.UNKNOWN);
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {
			if (!isConnected) {
				setState(ShutterStatus.UNKNOWN);
			}
		}
	};
	
	private final Subscription sourceSubscription;
	private final SettableUpdatedValue<String> text;
	
	public ShutterState(InitialiseOnSubscribeObservable<ShutterStatus> source) {
		text = new SettableUpdatedValue<>();
		sourceSubscription = source.subscribe(sourceObserver);
	}

	public UpdatedValue<String> text() {
		return text;
	}
	
	
	@Override
	public void close() {
		sourceSubscription.cancel();
	}
	
	private void setState(ShutterStatus state) {
		text.setValue(state.name());
	}
}
