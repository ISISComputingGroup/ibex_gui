package uk.ac.stfc.isis.ibex.epics.observing;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;

/**
 * The abstract base class for observables
 *
 */
public abstract class BaseObservable<T> implements Observable<T> {
		
	private final Collection<Observer<T>> observers = new CopyOnWriteArrayList<>();
	
	@Override
	public Subscription subscribe(Observer<T> observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
		
		return new Unsubscriber<Observer<T>>(observers, observer);
	}
		
	protected void setValue(T value) {
		if (value == null) {
			return;
		}
		
		for (Observer<T> observer : observers) {
			observer.onValue(value);
		}
	}

	protected void setError(Exception e) {
		for (Observer<T> observer : observers) {
			observer.onError(e);
		}
	}
	
	protected void setConnectionChanged(boolean isConnected) {
		for (Observer<T> observer : observers) {
			observer.onConnectionChanged(isConnected);
		}
	}
}
