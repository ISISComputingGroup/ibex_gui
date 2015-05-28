package uk.ac.stfc.isis.ibex.epics.writing;

import org.apache.logging.log4j.Logger;

public class LoggingForwardingWriter<T> extends ForwardingWriter<T, T> {

	protected final Logger log;
	protected final String id;
	
	public LoggingForwardingWriter(Logger log, String id, ConfigurableWriter<T, T> writer) {
		this.log = log;
		this.id = id;
		setWriter(writer);
	}

	@Override
	public void write(T value) {
		logValue(value);
		super.write(value);
	}
	
	@Override
	public void onError(Exception e) {
		log(e.toString());
	}

	@Override
	public void onCanWriteChanged(boolean canWrite) {		
		// do nothing
	}

	protected void logValue(T value) {
		log(value.toString());
	}

	private void log(String text) {
		log.info(id + " " + text);
	}
}
