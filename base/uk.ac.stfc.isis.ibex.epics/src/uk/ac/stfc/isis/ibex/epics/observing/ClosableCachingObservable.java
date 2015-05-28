package uk.ac.stfc.isis.ibex.epics.observing;

/**
 * The interface for a closable caching observable.
 *
 */
public interface ClosableCachingObservable<T> extends ClosableObservable<T>, CachingObservable<T> {

}
