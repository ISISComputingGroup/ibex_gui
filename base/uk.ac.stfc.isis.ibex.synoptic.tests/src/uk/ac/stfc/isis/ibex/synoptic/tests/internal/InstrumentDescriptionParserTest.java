
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

package uk.ac.stfc.isis.ibex.synoptic.tests.internal;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.synoptic.internal.InstrumentDescriptionParser;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;

/**
 * This class is responsible for testing the Instrument Description Parser 
 *
 */

@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:methodname", "unchecked" })
public class InstrumentDescriptionParserTest {

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.synoptic.internal.InstrumentDescriptionParser#convert(java.lang.String)}.
	 */
	@Test
	public final void convert_string_valid() {
		// Arrange
		InstrumentDescriptionParser parser = new InstrumentDescriptionParser();
		String expected = "Test";
		String value = "<?xml version=\"1.0\" ?>\n"
				+ "<instrument xmlns=\"http://www.isis.stfc.ac.uk//instrument\">"
				+ "	<name>" + expected + "</name>"
				+ "</instrument>";
		// Act
		try {
			SynopticDescription testDesc = parser.convert(value);
			assertEquals(expected, testDesc.name());
		} catch (ConversionException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public final void convert_string_invalid() {
		// Arrange
		InstrumentDescriptionParser parser = new InstrumentDescriptionParser();
		String expected = "Test";
		String value = expected;
		// Act
		try {
			SynopticDescription testDesc = parser.convert(value);
			fail("ConversionException not thrown");
		} catch (ConversionException e) {
			assertNotNull(e.getMessage());
		}
	}

}
