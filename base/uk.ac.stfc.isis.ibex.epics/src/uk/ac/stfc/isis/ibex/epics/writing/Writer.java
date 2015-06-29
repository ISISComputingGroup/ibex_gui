package uk.ac.stfc.isis.ibex.epics.writing;

/**
 * An interface for classes that will be writing, eventually, to a PV.
 *
 * @param <T> the type being written.
 */
public interface Writer<T> {

	void write(T value);
	
	void onError(Exception e);
	
	void onCanWriteChanged(boolean canWrite);
	
	boolean canWrite();
}
