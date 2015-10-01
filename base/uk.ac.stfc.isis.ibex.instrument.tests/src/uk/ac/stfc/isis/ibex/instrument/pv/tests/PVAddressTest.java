
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

package uk.ac.stfc.isis.ibex.instrument.pv.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.instrument.pv.PVAddress;

/**
 * This class is responsible for testing the PVAddress Class
 *
 */
@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:methodname" })
public class PVAddressTest {

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.instrument.pv.PVAddress#PVAddress(java.lang.String, java.lang.String)} 
	 * and {@link uk.ac.stfc.isis.ibex.instrument.pv.PVAddress#toString()}.
	 */
	@Test
	public final void new_pv_address_to_string() {
		// Arrange
		String prefix = "Prefix";
		String suffix = "Suffix";
		// Act
		PVAddress pvAddress = new PVAddress(prefix, suffix); 
		// Assert
		assertEquals(prefix+suffix, pvAddress.toString());
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.instrument.pv.PVAddress#setPrefix(java.lang.String)}.
	 */
	@Test
	public final void set_new_prefix_to_string() {
		// Arrange
		String prefix = "Prefix";
		String suffix = "Suffix";
		String newPrefix = "NewPrefix";
		PVAddress pvAddress = new PVAddress(prefix, suffix); 
		// Act
		pvAddress.setPrefix(newPrefix);
		// Assert
		assertEquals(newPrefix+suffix, pvAddress.toString());
	}

}
