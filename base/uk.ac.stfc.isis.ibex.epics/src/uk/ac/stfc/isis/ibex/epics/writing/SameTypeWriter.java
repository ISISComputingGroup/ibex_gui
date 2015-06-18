package uk.ac.stfc.isis.ibex.epics.writing;


/**
 * A class for writing data which does not require transformation before outputting.
 * The TransformingWriter is the contrast of this.
 *
 * @param <T> the type being written
 */
public class SameTypeWriter<T> extends BaseWriter<T, T> {
	@Override
	public void write(T value) {
		writeToWritables(value);
	}
}
