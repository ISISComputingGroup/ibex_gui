package uk.ac.stfc.isis.ibex.epics.conversion;


/**
 * Abstract base class for converters.
 *
 */
public abstract class Converter<A, B> {
			
	public <C> Converter<A, C> apply(final Converter<B, C> converter) {
		final Converter<A, B> self = this;
		return new Converter<A, C>() {
			public C convert(A value) throws ConversionException {
				return converter.convert(self.convert(value));
			}
		};
	}
	
	public abstract B convert(A value) throws ConversionException;
}
   
