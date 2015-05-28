package uk.ac.stfc.isis.ibex.epics.observing;

import org.apache.logging.log4j.Logger;


/**
 * Writes any changes to the observable to a logger.
 *
 */
public class LoggingObserver<T> extends BaseObserver<T> {

	protected final Logger log;
	protected final String id;
	
	public LoggingObserver(Logger log, String id) {
		this.log = log;
		this.id = id;
	}
	
	@Override
	public void onValue(T value) {
		log.info(id + " value: " + value);
	}

	@Override
	public void onError(Exception e) {
		log.error(id + " error: " + e);
	}

	@Override
	public void onConnectionChanged(boolean isConnected) {
		log.info(id + " connected: " + isConnected);
	}
}
