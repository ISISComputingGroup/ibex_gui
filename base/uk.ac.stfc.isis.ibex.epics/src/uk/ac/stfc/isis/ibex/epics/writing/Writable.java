package uk.ac.stfc.isis.ibex.epics.writing;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;

public interface Writable<T> {

	boolean canWrite();

	void write(T value);
	
	/**
	 * Allows the writer to receive updates from the writable.
	 * 
	 * @param writer
	 * @return a subscription on which to listen
	 */
	Subscription subscribe(ConfigurableWriter<?,?> writer);
}
