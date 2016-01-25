package uk.ac.stfc.isis.ibex.epics.tests.conversion;

import org.junit.Test;

import static org.junit.Assert.*;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Convert;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

@SuppressWarnings({ "checkstyle:methodname", "checkstyle:magicnumber" })
public class ConvertTest {

	private static String testHexed = "74657374";
	private static byte[] testZippedHexed = {55, 56, 57, 99, 50, 98, 52, 57,
			50, 100, 50, 101, 48, 49, 48, 48, 48, 52, 53, 100, 48, 49, 99, 49 };
	
	@Test
	public void convert_from_zipped_hex() throws ConversionException {
		//Arrange
		Converter<byte[], String> converter = Convert.fromZippedHex();
		
		String expected = "test";
		
		//Act
		String result = converter.convert(testZippedHexed);
		
		//Assert
		assertEquals(expected, result);
	}
		
	@Test
	public void convert_to_zipped_hex() throws ConversionException {
		//Arrange
		Converter<String, byte[]> converter = Convert.toZippedHex();
		
		String test = "test";
				
		//Act
		byte[] result = converter.convert(test);
		
		//Assert
		assertArrayEquals(testZippedHexed, result);
	}	
	
	@Test
	public void convert_from_hex() throws ConversionException {
		//Arrange
		Converter<String, String> converter = Convert.fromHexString();
		
		String expected = "test";
		
		//Act
		String result = converter.convert(testHexed);
		
		//Assert
		assertEquals(expected, result);
		
	}
	
	@Test
	public void convert_to_hex() throws ConversionException {
		//Arrange
		Converter<String, String> converter = Convert.toHexString();
		
		String test = "test";
		
		//Act
		String result = converter.convert(test);
		
		//Assert
		assertEquals(testHexed, result);
	}
	
	@Test
	public void convert_yes_to_boolean() throws ConversionException {
		//Arrange
		Converter<String, Boolean> converter = Convert.toBoolean();
		
		//Act
		boolean result = converter.convert("YES");
		
		//Assert
		assertEquals(true, result);
	}
	
	@Test
	public void convert_no_to_boolean() throws ConversionException {
		//Arrange
		Converter<String, Boolean> converter = Convert.toBoolean();
		
		//Act
		boolean result = converter.convert("NO");
		
		//Assert
		assertEquals(false, result);
	}
}
