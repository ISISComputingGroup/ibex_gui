package uk.ac.stfc.isis.ibex.epics.adapters;

import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialisableObserver;
import uk.ac.stfc.isis.ibex.epics.observing.InitialiseOnSubscribeObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closable;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;

public class UpdatedObservableAdapter<T> extends SettableUpdatedValue<T> implements Closable {

	private Subscription subscription;
	
	private final InitialisableObserver<T> observer = new BaseObserver<T>() {
		@Override
		public void onValue(T value) {
			setValue(value);
		}

		@Override
		public void onError(Exception e) {
			error(e);
		}

		@Override
		public void onConnectionChanged(boolean isConnected) {
			connectionChanged(isConnected);
		}
	};
	
	public UpdatedObservableAdapter(InitialiseOnSubscribeObservable<T> observable) {
		subscribeTo(observable);
	}
		
	protected void error(Exception e) {
	};
	
	protected void connectionChanged(boolean isConnected) {
		if (!isConnected) {
			setValue(null);
		}
	};
	
	@Override
	public void close() {
		subscription.cancel();
	}
	
	private void subscribeTo(InitialiseOnSubscribeObservable<T> observable) {
		subscription = observable.subscribe(observer);
	}
}
