
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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

@SuppressWarnings({ "checkstyle:methodname", "checkstyle:magicnumber" })
public class WritingXmlFromPeriodSettingsTest extends FileReadingTest {

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
	public void period_type_is_updated() {
		
		Period period = periodSettings.getPeriods().get(7);
		PeriodType newType = PeriodType.DAQ;
		
		assertThat(period.getType(), is(not(newType)));
		period.setType(newType);
		reloadSettingsFromCurrentValues();
		
		period = periodSettings.getPeriods().get(7);
		assertThat(period.getType(), is(newType));
	}
	
	@Test
	public void period_frames_is_updated() {
		Period period = periodSettings.getPeriods().get(2);
		int newValue = 10;
		
		assertThat(period.getFrames(), is(not(newValue)));
		period.setFrames(newValue);
		reloadSettingsFromCurrentValues();
		
		period = periodSettings.getPeriods().get(2);
		assertThat(period.getFrames(), is(newValue));
	}

	@Test
	public void period_binary_output_is_updated() {
		Period period = periodSettings.getPeriods().get(3);
		int newValue = 2;
		
		assertThat(period.getBinaryOutput(), is(not(newValue)));
		period.setBinaryOutput(newValue);
		reloadSettingsFromCurrentValues();
		
		period = periodSettings.getPeriods().get(3);
		assertThat(period.getBinaryOutput(), is(newValue));
	}
	
	@Test
	public void period_label_is_updated() {
		Period period = periodSettings.getPeriods().get(4);
		String newValue = "Test label";
		
		assertThat(period.getLabel(), is(not(newValue)));
		period.setLabel(newValue);
		reloadSettingsFromCurrentValues();
		
		period = periodSettings.getPeriods().get(4);
		assertThat(period.getLabel(), is(newValue));
	}
	
	@Test
	public void setup_source_is_updated() {
		PeriodSetupSource newValue = PeriodSetupSource.PARAMETERS;
		
		assertThat(periodSettings.getSetupSource(), is(not(newValue)));
		periodSettings.setSetupSource(newValue);
		reloadSettingsFromCurrentValues();
		
		assertThat(periodSettings.getSetupSource(), is(newValue));
	}
	
	@Test
    public void setup_source_is_not_updated_if_null() {

        // Arrange: Check that null is not the current setup source.
        assertNotEquals(periodSettings.getSetupSource(), null);
        
        // Act: Try to set a setup source as null.
        periodSettings.setSetupSource(null);
        reloadSettingsFromCurrentValues();
        
        // Assert: Check that null has been ignored.
        assertNotEquals(periodSettings.getSetupSource(), null);       
    }
	

	@Test
	public void period_file_is_updated() {
		String newValue = "new_path";
		
		assertThat(periodSettings.getNewPeriodFile(), is(not(newValue)));
		periodSettings.setNewPeriodFile(newValue);
		reloadSettingsFromCurrentValues();
		
		assertThat(periodSettings.getNewPeriodFile(), is(newValue));
	}
	
	@Test
	public void period_control_type_is_updated() {
		PeriodControlType newValue = PeriodControlType.SOFTWARE;
		
		assertThat(periodSettings.getPeriodType(), is(not(newValue)));
		periodSettings.setPeriodType(newValue);
		reloadSettingsFromCurrentValues();
		
		assertThat(periodSettings.getPeriodType(), is(newValue));		
	}
	
	@Test
    public void period_control_type_is_not_updated_if_null() {

	    // Arrange: Check that null is not the current period type.
        assertNotEquals(periodSettings.getPeriodType(), null);
        
        // Act: Try to set a period type as null.
        periodSettings.setPeriodType(null);
        reloadSettingsFromCurrentValues();
        
        // Assert: Check that null has been ignored.
        assertNotEquals(periodSettings.getPeriodType(), null);       
    }
	
	@Test
	public void software_periods_is_updated() {	
		int newValue = 12121;
		
		assertThat(periodSettings.getSoftwarePeriods(), is(not(newValue)));
		periodSettings.setSoftwarePeriods(newValue);
		reloadSettingsFromCurrentValues();
		
		assertThat(periodSettings.getSoftwarePeriods(), is(newValue));		
	}
	
	@Test
	public void hardware_periods_is_updated() {
		double newValue = 1.11;
		
		assertThat(periodSettings.getHardwarePeriods(), is(not(newValue)));
		periodSettings.setHardwarePeriods(newValue);
		reloadSettingsFromCurrentValues();
		
		assertThat(periodSettings.getHardwarePeriods(), is(newValue));			
	}
	
	@Test
	public void output_delay_is_updated() {
		double newValue = 1d;
		
		assertThat(periodSettings.getOutputDelay(), is(not(newValue)));
		periodSettings.setOutputDelay(newValue);
		reloadSettingsFromCurrentValues();
		
		assertThat(periodSettings.getOutputDelay(), is(newValue));
	}
	
	private void reloadSettingsFromCurrentValues() {
		periodSettings.setXml(periodSettings.xml());		
	}
}
