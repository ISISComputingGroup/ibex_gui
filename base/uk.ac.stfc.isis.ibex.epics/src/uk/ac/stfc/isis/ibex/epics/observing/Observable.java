package uk.ac.stfc.isis.ibex.epics.observing;


/**
 * The observable interface for creating a subscription.
 *
 */
public interface Observable<T> {
	Subscription subscribe(Observer<T> observer);
}
