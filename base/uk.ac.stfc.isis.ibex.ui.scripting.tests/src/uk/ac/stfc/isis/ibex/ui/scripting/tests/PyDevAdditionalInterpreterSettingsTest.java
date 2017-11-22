
/**
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

package uk.ac.stfc.isis.ibex.ui.scripting.tests;

import static org.junit.Assert.*;

import java.util.Collection;

import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.ui.scripting.PyDevAdditionalInterpreterSettings;

@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:methodname" })
public class PyDevAdditionalInterpreterSettingsTest {
	
	IPreferencesService mockPreferencesService;
	private PyDevAdditionalInterpreterSettings settings;
	
	@Before public void initMocks() {
		mockPreferencesService = mock(IPreferencesService.class);
		Instrument mockInstrument = mock(Instrument.class);
		InstrumentInfo instrument = new InstrumentInfo("dummy", "dummy", "dummy");
		when(mockInstrument.currentInstrument()).thenReturn(instrument);
        settings = new PyDevAdditionalInterpreterSettings(mockPreferencesService, mockInstrument);
    }

    @Test
    public void GIVEN_auto_addr_list_is_false_WHEN_get_env_THEN_it_is_no() {
    	when(mockPreferencesService.getBoolean(anyString(), anyString(), anyBoolean(), (IScopeContext[]) any())).thenReturn(false);
    	   	
    	
    	Collection<String> vars = settings.getAdditionalEnvVariables();
    	String result = getEnvironmentVariableValue(vars, "EPICS_CA_AUTO_ADDR_LIST=");
    	
        // Assert
        assertEquals("NO", result);
    }

    @Test
    public void GIVEN_auto_addr_list_is_true_WHEN_get_env_THEN_it_is_no() {
    	when(mockPreferencesService.getBoolean(anyString(), anyString(), anyBoolean(), (IScopeContext[]) any())).thenReturn(true);
    	   	
    	
    	Collection<String> vars = settings.getAdditionalEnvVariables();
    	String result = getEnvironmentVariableValue(vars, "EPICS_CA_AUTO_ADDR_LIST=");
    	
        // Assert
        assertEquals("YES", result);
    }

	private String getEnvironmentVariableValue(Collection<String> vars, String environmentVaribaleName) {
		String result = "";
    	for (String var : vars) {
			if (var.startsWith(environmentVaribaleName)) {
    			result = var.substring(environmentVaribaleName.length());
    		}
    	}
		return result;
	}

}
