package uk.ac.stfc.isis.ibex.epics.writing;

/**
 * The abstract class for writers that need to transform data before writing it.
 * The SameTypeWriter is the contrast of this.
 *
 * @param <TIn> the type to be converted from
 * @param <TOut> the type to convert to
 */
public abstract class TransformingWriter<TIn, TOut> extends BaseWriter<TIn, TOut> {

	public void write(TIn value) {
		TOut transformed = transform(value);
		if (transformed != null) {
			super.writeToWritables(transformed);
		}
	}
	
	protected abstract TOut transform(TIn value);
}
