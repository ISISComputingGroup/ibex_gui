package uk.ac.stfc.isis.ibex.epics.observing;

/**
 * An observable whose source can be changed.
 */
public class SwitchableObservable<T> extends ForwardingObservable<T> {
	
	public SwitchableObservable(CachingObservable<T> source) {
		switchTo(source);
	}

	public void switchTo(CachingObservable<T> newSource) {
		setSource(newSource);
	}
}
