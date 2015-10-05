
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

package uk.ac.stfc.isis.ibex.configserver.tests.json;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.configserver.configuration.Macro;
import uk.ac.stfc.isis.ibex.configserver.internal.IocParameters;
import uk.ac.stfc.isis.ibex.configserver.json.IocsParametersConverter;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;

public class IocsParameterConverterTest {
	private String exampleJson = "";

	@Before
	public void setUp() {
		exampleJson = "{\"TEST_01\": {\"running\": false}, \"SDTEST_03\": ";
		exampleJson += 
		"{\"macros\": [{\"pattern\": \"^COM[0-9]+$\", \"description\": \"Serial COM port to connect to\", \"name\": \"PORT8\"}], ";
		exampleJson += "\"running\": false, \"pvs\": [], \"pvsets\": []}}";
			
	}

	@Test
	public void conversion_of_valid_json_works() throws ConversionException {
		// Arrange
		IocsParametersConverter conv = new IocsParametersConverter();
		
		// Act
		Map<String, IocParameters> pars = conv.convert(exampleJson);

		// Assert
		assertTrue(pars.keySet().contains("TEST_01"));
		assertTrue(pars.keySet().contains("SDTEST_03"));
		
		// Check the parameters
		IocParameters testPars = pars.get("TEST_01");
		assertEquals(testPars.isRunning(), false);
		
		IocParameters sdTestPars = pars.get("SDTEST_03");
		assertEquals(sdTestPars.isRunning(), false);
		assertEquals(sdTestPars.getPVs().size(), 0);
		assertEquals(sdTestPars.getPVSets().size(), 0);
		List<Macro> macros = (List<Macro>) sdTestPars.getMacros();
		assertEquals(macros.get(0).getName(), "PORT8");
		assertEquals(macros.get(0).getPattern(), "^COM[0-9]+$");
		assertEquals(macros.get(0).getDescription(), "Serial COM port to connect to");
	}
	
	@Test
	public void add_property_change_listener_does_not_throw() throws ConversionException {
		// Back-story: if GSON does not find a default constructor for Macro
		// then the constructor is not called and the object created is not
		// a fully initialised Macro object.
		// Adding a property change listener will throw if this is the case.
		
		// Arrange
		IocsParametersConverter conv = new IocsParametersConverter();
		
		// Act
		Map<String, IocParameters> pars = conv.convert(exampleJson);
		IocParameters sdTestPars = pars.get("SDTEST_03");
		List<Macro> macros = (List<Macro>) sdTestPars.getMacros();
		Macro macro = macros.get(0);
		macro.addPropertyChangeListener(null);

		// Assert - nothing to do		
	}
}
