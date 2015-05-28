package uk.ac.stfc.isis.ibex.epics.observing;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

/**
 * Converts the source data to a specific type via a converter.
 *
 */
public class ConvertingObservable<R ,T> extends TransformingObservable<R, T> {

	private final Converter<R, T> formatter;
	
	public ConvertingObservable(ClosableCachingObservable<R> source, Converter<R, T> formatter) {
		this.formatter = formatter;
		setSource(source);
	}
	
	@Override
	protected T transform(R value) {
		try {
			if (value == null) {
				throw new ConversionException("value is null");
			}
			
			return formatter.convert(value);
		} catch (ConversionException e) {
			setError(e);
		}
		
		return null;
	}
}
