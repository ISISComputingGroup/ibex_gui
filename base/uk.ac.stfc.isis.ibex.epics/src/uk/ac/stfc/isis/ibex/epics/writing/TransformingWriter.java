package uk.ac.stfc.isis.ibex.epics.writing;

public abstract class TransformingWriter<TIn, TOut> extends BaseWriter<TIn, TOut> {

	public void write(TIn value) {
		TOut transformed = transform(value);
		if (transformed != null) {
			super.writeToWritables(transformed);
		}
	}
	
	protected abstract TOut transform(TIn value);
}
