package uk.ac.stfc.isis.ibex.epics.writing;

public interface Writer<T> {

	void write(T value);
	
	void onError(Exception e);
	
	void onCanWriteChanged(boolean canWrite);
	
	boolean canWrite();
}
