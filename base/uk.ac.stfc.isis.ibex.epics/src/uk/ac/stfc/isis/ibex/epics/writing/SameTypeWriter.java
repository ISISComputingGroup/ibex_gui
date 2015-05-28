package uk.ac.stfc.isis.ibex.epics.writing;

public class SameTypeWriter<T> extends BaseWriter<T, T> {
	@Override
	public void write(T value) {
		writeToWritables(value);
	}
}
