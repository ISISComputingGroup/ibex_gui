package uk.ac.stfc.isis.ibex.epics.writing;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

public class ConvertingWritable<TIn, TOut> extends ForwardingWritable<TIn, TOut> {

	private final Converter<TIn, TOut> converter;

	public ConvertingWritable(Writable<TOut> destination, Converter<TIn, TOut> converter) {
		this.converter = converter;
		setWritable(destination);
	}
	
	@Override
	protected TOut transform(TIn value) {
		try {
			return converter.convert(value);
		} catch (ConversionException e) {
			error(e);
		}
		
		return null;
	}

}
