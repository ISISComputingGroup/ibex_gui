package uk.ac.stfc.isis.ibex.epics.observing;

import java.util.ArrayList;
import java.util.Collection;

public class Unsubscriber<T> implements Subscription {

	private final Collection<T> observers;
	private T observer;
	
	public Unsubscriber(Collection<T> observers, T observer) {
		this.observers = observers != null ? observers : new ArrayList<T>();
		this.observer = observer;
	}
	
	@Override
	public void cancel() {
		if (observer != null && observers.contains(observer)) {
			observers.remove(observer);
		}
	}
}
