package uk.ac.stfc.isis.ibex.epics.tests.conversion;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.DateTimeFormatter;
import uk.ac.stfc.isis.ibex.epics.conversion.ElapsedTimeFormatter;
import static org.junit.Assert.*;

public class DateTimeFormatTest {

	@Test
	public void millis_pattern_string_to_date() throws ConversionException {
		//Arrange
		DateTimeFormatter converter = new DateTimeFormatter();
		
		String test = "1992/02/07 00:00:01.000";
		
		//Act
		String result = converter.convert(test);
		
		//Assert
		assertEquals("07/02/1992 00:00:01", result);
	}
	
	@Test
	public void us_pattern_string_to_date() throws ConversionException {
		//Arrange
		DateTimeFormatter converter = new DateTimeFormatter();
		
		String test = "02/07/1992 00:00:00";
		
		//Act
		String result = converter.convert(test);
		
		//Assert
		assertEquals("07/02/1992 00:00:00", result);
	}
	
	@Test
	public void iso_pattern_string_to_date() throws ConversionException {
		//Arrange
		DateTimeFormatter converter = new DateTimeFormatter();
		
		String test = "1992-02-07T00:00:00";
		
		//Act
		String result = converter.convert(test);
		
		//Assert
		assertEquals("07/02/1992 00:00:00", result);
	}
	
	@Test
	public void elapsed_seconds_to_time() throws ConversionException {
		//Arrange
		ElapsedTimeFormatter converter = new ElapsedTimeFormatter();
		
		//Act
		String result = converter.convert((long) 10000);
		
		//Assert
		assertEquals("2 hours 46 min 40 s", result);
	}
	
	@Test
	public void elapsed_seconds_to_time_seconds_only() throws ConversionException {
		//Arrange
		ElapsedTimeFormatter converter = new ElapsedTimeFormatter();
		
		//Act
		String result = converter.convert((long) 10);
		
		//Assert
		assertEquals("10 s", result);
	}
	
	@Test
	public void elapsed_seconds_to_time_seconds_and_minutes_only() throws ConversionException {
		//Arrange
		ElapsedTimeFormatter converter = new ElapsedTimeFormatter();
		
		//Act
		String result = converter.convert((long) 100);
		
		//Assert
		assertEquals("1 min 40 s", result);
	}
}
