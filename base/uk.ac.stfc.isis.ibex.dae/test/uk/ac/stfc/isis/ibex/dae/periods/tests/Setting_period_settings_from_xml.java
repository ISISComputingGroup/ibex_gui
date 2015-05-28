package uk.ac.stfc.isis.ibex.dae.periods.tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.Period;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodControlType;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodSetupSource;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodType;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.XMLBackedPeriodSettings;
import uk.ac.stfc.isis.ibex.dae.tests.FileReadingTest;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Before;
import org.junit.Test;


public class Setting_period_settings_from_xml extends FileReadingTest {

	private XMLBackedPeriodSettings periodSettings;
	
	@Override
	protected URL fileLocation() throws MalformedURLException {
		return getClass().getResource("/uk/ac/stfc/isis/ibex/dae/periods/tests/period_settings.xml");
	}
	
	@Before
	public void setup() throws IOException {
		periodSettings = new XMLBackedPeriodSettings();
		periodSettings.setXml(fileContent());
	}

	@Test
	public void eight_periods_are_read() {
		assertThat(periodSettings.getPeriods().size(), is(8));
	}
	
	@Test
	public void first_period_is_updated() {
		Period period = periodSettings.getPeriods().get(0);
		assertThat(period.getType(), is(PeriodType.DWELL));
		assertThat(period.getFrames(), is(1));
		assertThat(period.getBinaryOutput(), is(1));
		assertThat(period.getLabel(), is("Test"));
	}
	
	@Test
	public void setup_source_is_updated() {
		assertThat(periodSettings.getSetupSource(), is(PeriodSetupSource.FILE));
	}
	
	@Test
	public void period_file_is_updated() {
		assertThat(periodSettings.getPeriodFile(), is("path_to_period_file"));
	}
	
	@Test
	public void period_type_is_updated() {
		assertThat(periodSettings.getPeriodType(), is(PeriodControlType.HARDWARE_DAE));
	}
	
	@Test
	public void software_periods_is_updated() {
		assertThat(periodSettings.getSoftwarePeriods(), is(12345));
	}
	
	@Test
	public void hardware_periods_is_updated() {
		assertThat(periodSettings.getHardwarePeriods(), is(3.1415));
	}
	
	@Test
	public void output_delay_is_updated() {
		assertThat(periodSettings.getOutputDelay(), is(20000d));
	}
}
