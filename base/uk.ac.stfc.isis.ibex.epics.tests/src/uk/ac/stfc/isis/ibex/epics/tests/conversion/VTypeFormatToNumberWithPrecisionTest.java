package uk.ac.stfc.isis.ibex.epics.tests.conversion;

import static org.junit.Assert.*;

import org.epics.vtype.Display;
import org.epics.vtype.VNumber;
import org.epics.vtype.VDouble;
import org.epics.vtype.ValueFactory;
import org.epics.util.text.NumberFormats;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;
import uk.ac.stfc.isis.ibex.epics.conversion.VTypeFormat;

@SuppressWarnings({"checkstyle:magicnumber", "checkstyle:localvariablename", "checkstyle:methodname"})
public class VTypeFormatToNumberWithPrecisionTest {

	@Test
	public void conversion_no_precision() throws ConversionException {
		// Arrange
		Converter<VNumber, Number> converter = VTypeFormat.toNumberWithPrecision();
		VDouble vnum = ValueFactory.newVDouble(123.456);
		
		// Act
		Number result = converter.convert(vnum);
		
		// Assert
		assertEquals(result, 123.456);
	}
	
	@Test
	public void conversion_with_precision() throws ConversionException {
		// Arrange
		Converter<VNumber, Number> converter = VTypeFormat.toNumberWithPrecision();
		
		Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "", NumberFormats.format(1), 0.0, 0.0, 0.0, 0.0, 0.0);
		
		VDouble vnum = ValueFactory.newVDouble(123.456, display);
		
		// Act
		Number result = converter.convert(vnum);
		
		// Assert
		assertEquals(result, 123.5);
	}
	
	@Test
	public void conversion_with_precision_greater_than_digits() throws ConversionException {
		// Arrange
		Converter<VNumber, Number> converter = VTypeFormat.toNumberWithPrecision();
		
		Display display = ValueFactory.newDisplay(0.0, 0.0, 0.0, "", NumberFormats.format(5), 0.0, 0.0, 0.0, 0.0, 0.0);
		
		VDouble vnum = ValueFactory.newVDouble(123.4, display);
		
		// Act
		Number result = converter.convert(vnum);
		
		// Assert
		assertEquals(result, 123.4);
	}
	
}
