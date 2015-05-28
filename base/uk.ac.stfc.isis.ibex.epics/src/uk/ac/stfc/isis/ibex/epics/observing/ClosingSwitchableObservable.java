package uk.ac.stfc.isis.ibex.epics.observing;

import uk.ac.stfc.isis.ibex.epics.pv.Closable;

/**
 * An observable which closes its current source when switching to another.
 */
public class ClosingSwitchableObservable<T> extends SwitchableObservable<T> implements ClosableCachingObservable<T> {
	
	private Closable source;
	
	public ClosingSwitchableObservable(ClosableCachingObservable<T> source) {
		super(source);
		this.source = source;
	}

	public void switchTo(ClosableCachingObservable<T> newSource) {
		source.close();
		source = newSource;
		setSource(newSource);
	}

	@Override
	public void close() {
		source.close();
		super.close();
	}
}
