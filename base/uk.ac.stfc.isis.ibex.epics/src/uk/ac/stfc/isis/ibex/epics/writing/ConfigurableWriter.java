package uk.ac.stfc.isis.ibex.epics.writing;

import uk.ac.stfc.isis.ibex.epics.observing.Subscription;

/**
 * An interface for classes that observe and write to a Writable.
 *
 * @param <TIn> the type coming in
 * @param <TOut> the type written out
 */
public interface ConfigurableWriter<TIn, TOut> extends Writer<TIn> {
	
	/**
	 * Add a target for the writer to write to.
	 * 
	 * @param writable
	 * @return a subscription
	 */
	Subscription writeTo(Writable<TOut> writable);
}
