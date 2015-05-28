package uk.ac.stfc.isis.ibex.epics.writing;

import uk.ac.stfc.isis.ibex.epics.pv.Closable;

public class ClosingWritable<T> extends ForwardingWritable<T, T> implements ClosableWritable<T> {

	private Closable resource;
	
	public ClosingWritable(ClosableWritable<T> destination) {
		setWritable(destination);
	}
	
	public void setWritable(ClosableWritable<T> destination) {
		super.setWritable(destination);
		closeResource();
		resource = destination;
	}

	@Override
	protected T transform(T value) {
		return value;
	}

	@Override
	public void close() {
		closeResource();
	}

	private void closeResource() {
		if (resource != null) {
			resource.close();
		}
	};
}
