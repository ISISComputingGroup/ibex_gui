package uk.ac.stfc.isis.ibex.ui.tests;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.ui.Utils;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the InstrumentUtils class
 */
public class UtilsTest {
	
	@Test
	public void WHEN_value_between_min_and_max_THEN_contrain_returns_value() {
		int min = -2;
		int value = 7;
		int max = 20;
		int expected = Utils.constrainIntToRange(value, min, max);
		assertEquals("Should have returned the value as it is between the min and max values", 
				value, expected );
	}
	
	@Test
	public void WHEN_value_greater_than_max_THEN_constrain_returns_max() {
		int min = 10;
		int value = 20;
		int max = 15;
		assertEquals("Should have returned the max as the value was greater than it", 
				Utils.constrainIntToRange(value, min, max), max);
	}
	
	@Test
	public void WHEN_value_less_than_min_THEN_costrain_returns_min() {
		int min = 10;
		int value = 7;
		int max = 20;
		assertEquals("Should have returned the min as the min is less than the value", 
				Utils.constrainIntToRange(value, min, max), min);
	}
	
	@Test
	public void WHEN_value_is_less_than_min_and_greater_than_max_THEN_constrain_throws_exception() {
		int min = 20;
		int value = 7;
		int max = 10;
		assertEquals("Should have returned the max as the max is less than the value", 
				Utils.constrainIntToRange(value, min, max), max);
	}

}
