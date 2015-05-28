package uk.ac.stfc.isis.ibex.epics.writing;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;

// Observes and writes to a Writable
public interface ConfigurableWriter<TIn, TOut> extends Writer<TIn> {
	// Add a target for the writer to write to
	Subscription writeTo(Writable<TOut> writable);
}
