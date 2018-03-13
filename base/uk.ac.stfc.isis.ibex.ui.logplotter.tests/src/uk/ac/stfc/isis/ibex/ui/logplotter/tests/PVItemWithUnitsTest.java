package uk.ac.stfc.isis.ibex.ui.logplotter.tests;

import org.junit.Test;
import uk.ac.stfc.isis.ibex.ui.logplotter.PVItemWithUnits;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the PVItemWithUnits class.
 */
public class PVItemWithUnitsTest {
	@Test
    public void WHEN_get_egu_pv_is_called_with_a_bare_pv_THEN_appends_egu_field() {
    	String actual = PVItemWithUnits.getEguPv("MY:TEST:PV");
    	String expected = "MY:TEST:PV.EGU";
    	
    	assertEquals(actual, expected);
    }
	
	@Test
    public void WHEN_get_egu_pv_is_called_with_a_pv_with_val_field_THEN_removes_val_and_appends_egu_field() {
    	String actual = PVItemWithUnits.getEguPv("MY:TEST:PV.VAL");
    	String expected = "MY:TEST:PV.EGU";
    	
    	assertEquals(actual, expected);
    }
	
	@Test
    public void WHEN_get_egu_pv_is_called_with_a_pv_with_a_non_val_field_THEN_returns_null() {
    	String actual = PVItemWithUnits.getEguPv("MY:TEST:PV.PREC");
    	String expected = null;
    	
    	assertEquals(actual, expected);
    }
}
