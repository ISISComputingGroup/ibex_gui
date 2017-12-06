package uk.ac.stfc.isis.ibex.dae.tests.spectra;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.osgi.service.prefs.Preferences;

import uk.ac.stfc.isis.ibex.dae.spectra.Spectrum;
import uk.ac.stfc.isis.ibex.dae.spectra.SpectrumYAxisTypes;

public class SpectrumTests {
	
	private final double[] CONSTANT_BIN_WIDTH_X_DATA = {1, 2, 3, 4, 5};
	private final double[] VARIABLE_BIN_WIDTH_X_DATA = {1, 2, 4, 8, 16};
	
	private final double[] Y_DATA = {1, 2, 1, 2, 1};
	
	private Spectrum spectrum;
	
	@Before
	public void setUp() {
		
		if (CONSTANT_BIN_WIDTH_X_DATA.length != VARIABLE_BIN_WIDTH_X_DATA.length 
				|| VARIABLE_BIN_WIDTH_X_DATA.length != Y_DATA.length) {
			fail("All test data should have the same length.");
		}
		
		Preferences preferenceMock = Mockito.mock(Preferences.class);
		Mockito.doNothing().when(preferenceMock).putInt(Mockito.anyString(), Mockito.anyInt());
		Mockito.when(preferenceMock.getInt(Mockito.anyString(), Mockito.anyInt())).thenReturn(0);
		
		spectrum = new Spectrum(preferenceMock);
	}

	@Test
	public void test_GIVEN_y_axis_type_is_count_rate_WHEN_new_data_arrives_THEN_it_is_not_changed_when_set() {
		spectrum.setTypeSelectionIndex(Arrays.asList(SpectrumYAxisTypes.values()).indexOf(SpectrumYAxisTypes.COUNT_RATE));
		spectrum.setXData(Arrays.copyOf(CONSTANT_BIN_WIDTH_X_DATA, CONSTANT_BIN_WIDTH_X_DATA.length));
		spectrum.setYData(Arrays.copyOf(Y_DATA, Y_DATA.length));
		
		assertArrayEquals(Y_DATA, spectrum.yData(), 0);
	}
	

	@Test
	public void test_GIVEN_y_axis_type_is_absolute_counts_WHEN_new_data_arrives_THEN_it_is_not_changed_if_the_bin_widths_are_constant_and_one_except_for_the_last_element_being_set_to_zero() {
		spectrum.setTypeSelectionIndex(Arrays.asList(SpectrumYAxisTypes.values()).indexOf(SpectrumYAxisTypes.ABSOLUTE_COUNTS));
		spectrum.setXData(Arrays.copyOf(CONSTANT_BIN_WIDTH_X_DATA, CONSTANT_BIN_WIDTH_X_DATA.length));
		spectrum.setYData(Arrays.copyOf(Y_DATA, Y_DATA.length));
		
		// Loop from 0 to length - 1 because the last element is special 
		// (always zero because "bin width" can't be calculated for it).
		for (int i=0; i<Y_DATA.length - 1; i++){
			assertEquals(Y_DATA[i], spectrum.yData()[i], 0);
		}
		
		// Assert the last element has been set to zero.
		assertEquals(0, spectrum.yData()[spectrum.yData().length - 1], 0);
	}
	

	@Test
	public void test_GIVEN_y_axis_type_is_absolute_counts_WHEN_new_data_arrives_THEN_it_is_scaled_by_the_bin_widths() {
		spectrum.setTypeSelectionIndex(Arrays.asList(SpectrumYAxisTypes.values()).indexOf(SpectrumYAxisTypes.ABSOLUTE_COUNTS));
		spectrum.setXData(Arrays.copyOf(VARIABLE_BIN_WIDTH_X_DATA, VARIABLE_BIN_WIDTH_X_DATA.length));
		spectrum.setYData(Arrays.copyOf(Y_DATA, Y_DATA.length));
		
		// Loop from 0 to length - 1 because the last element is special 
		// (always zero because "bin width" can't be calculated for it).
		for (int i=0; i<Y_DATA.length - 1; i++){
			assertEquals(Y_DATA[i] * Math.abs(spectrum.xData()[i] - spectrum.xData()[i+1]), spectrum.yData()[i], 0);
		}
		
		// Assert the last element has been set to zero.
		assertEquals(0, spectrum.yData()[spectrum.yData().length - 1], 0);
	}

}
