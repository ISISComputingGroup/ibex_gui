package uk.ac.stfc.isis.ibex.epics.observing;


/**
 * An observable that performs a read when the subscription is created.
 *
 */
public class InitialiseOnSubscribeObservable<T> extends ForwardingObservable<T> {
	
	public InitialiseOnSubscribeObservable(CachingObservable<T> source) {
		setSource(source);
	}

	public Subscription subscribe(InitialisableObserver<T> observer) {
		observer.update(value(), lastError(), isConnected());
		return super.subscribe(observer);
	}
}