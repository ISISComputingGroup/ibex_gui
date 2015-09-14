package uk.ac.stfc.isis.ibex.configserver.tests.configuration;

import static org.junit.Assert.*;

import org.junit.Test;
import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;

public class MacroTest {

	@Test
	public void create_macro_using_string_parameters_constructor_works() {
		// Arrange
		// Act
		Macro m = new Macro("macro1", "value1", "a test macro", ".+");
		
		// Assert
		assertEquals(m.getName(), "macro1");
		assertEquals(m.getValue(), "value1");
		assertEquals(m.getDescription(), "a test macro");
		assertEquals(m.getPattern(), ".+");
	}
	
	@Test
	public void string_parameters_constructor_change_value_works() {
		// Arrange
		Macro m = new Macro("macro1", "value1", "a test macro", ".+");
		
		// Act
		m.setValue("new value");
		
		// Assert
		assertEquals(m.getValue(), "new value");
	}
	
	@Test
	public void create_macro_using_copy_constructor_works() {
		// Arrange
		Macro n = new Macro("macro1", "value1", "a test macro", ".+");
		
		// Act
		Macro m = new Macro(n);
		
		// Assert
		assertEquals(m.getName(), "macro1");
		assertEquals(m.getValue(), "value1");
		assertEquals(m.getDescription(), "a test macro");
		assertEquals(m.getPattern(), ".+");
	}
	
	@Test
	public void copy_constructor_change_value_works() {
		// Arrange
		Macro n = new Macro("macro1", "value1", "a test macro", ".+");
		Macro m = new Macro(n);
		
		// Act
		m.setValue("new value");
		
		// Assert
		assertEquals(m.getValue(), "new value");
	}
}
