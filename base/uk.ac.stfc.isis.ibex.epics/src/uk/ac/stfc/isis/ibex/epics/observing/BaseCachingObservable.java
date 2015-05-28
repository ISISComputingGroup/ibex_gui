package uk.ac.stfc.isis.ibex.epics.observing;

public class BaseCachingObservable<T> extends BaseObservable<T> implements CachingObservable<T> {
	
	private T value;
	private boolean isConnected;
	private Exception lastError;
		
	public T value() {
		return value;
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	public Exception lastError() {
		return lastError;
	}
	
	@Override
	protected void setValue(T value) {
		if (value == null) {
			return;
		}
		
		this.value = value;
		super.setValue(value);
	}
	
	@Override
	protected void setError(Exception e) {
		lastError = e;
		super.setError(e);
	}
	
	@Override
	protected void setConnectionChanged(boolean isConnected) {
		this.isConnected = isConnected;
		super.setConnectionChanged(isConnected);
	}
}
