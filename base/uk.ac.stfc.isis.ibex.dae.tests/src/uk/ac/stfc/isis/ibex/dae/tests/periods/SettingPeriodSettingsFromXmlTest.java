
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
* https://www.eclipse.org/org/documents/epl-v10.php or 
* http://opensource.org/licenses/eclipse-1.0.php
*/

package uk.ac.stfc.isis.ibex.dae.tests.periods;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.Period;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodControlType;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodSetupSource;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodType;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.XMLBackedPeriodSettings;
import uk.ac.stfc.isis.ibex.dae.tests.FileReadingTest;


public class SettingPeriodSettingsFromXmlTest extends FileReadingTest {

	private XMLBackedPeriodSettings periodSettings;
	
	@Override
	protected URL fileLocation() throws MalformedURLException {
        return getClass().getResource("/uk/ac/stfc/isis/ibex/dae/tests/periods/period_settings.xml");
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
