package uk.ac.stfc.isis.ibex.epics.observing;

/**
 * Holds a pair of items.
 *
 */
public class Pair<T1, T2> {

	public final T1 first;
	public final T2 second;
	
	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}
}
