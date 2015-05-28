package uk.ac.stfc.isis.ibex.configserver.tests.doubles;

import uk.ac.stfc.isis.ibex.model.WritableValue;

public class LoggingWritableValue<T> implements WritableValue<T> {

	private T value;
	
	public T lastValue() {		
		return value;
	}
	
	@Override
	public void set(T value) {
		this.value = value;		
	}
}
