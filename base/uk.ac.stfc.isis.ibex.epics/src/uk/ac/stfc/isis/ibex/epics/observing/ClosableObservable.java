package uk.ac.stfc.isis.ibex.epics.observing;

import uk.ac.stfc.isis.ibex.epics.pv.Closable;

/**
 * An observable that should be closed when observation is complete.
 */
public interface ClosableObservable<T> extends Observable<T>, Closable {

}
