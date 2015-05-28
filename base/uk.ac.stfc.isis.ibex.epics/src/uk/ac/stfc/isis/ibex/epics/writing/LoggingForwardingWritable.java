package uk.ac.stfc.isis.ibex.epics.writing;

import org.apache.logging.log4j.Logger;

public class LoggingForwardingWritable<T> extends ForwardingWritable<T, T>{

	private final Logger log;
	private final String id;

	public LoggingForwardingWritable(Logger log, String id, Writable<T> destination) {
		this.log = log;
		this.id = id;
		setWritable(destination);
	}
	
	@Override
	protected T transform(T value) {
		return value;
	}
	
	@Override
	public void write(T value) {
		super.write(value);
		log(value.toString());
	}

	private void log(String text) {
		log.info(id + " " + text);
	}
}
