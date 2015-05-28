package uk.ac.stfc.isis.ibex.epics.writing;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;

public interface Writable<T> {

	boolean canWrite();

	void write(T value);
	
	// Allow the writer to receive updates from the writable
	Subscription subscribe(ConfigurableWriter<?,?> writer);
}
