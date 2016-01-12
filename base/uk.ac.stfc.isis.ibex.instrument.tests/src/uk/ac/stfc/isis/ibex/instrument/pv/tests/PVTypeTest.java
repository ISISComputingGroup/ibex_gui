
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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.instrument.pv.PVType;

/**
 * This class is responsible for testing the values of the PVType ENUM for completion 
 *
 */
@SuppressWarnings({ "checkstyle:magicnumber", "checkstyle:methodname" })
public class PVTypeTest {

	@Test
	public final void local_pv_to_string() {
		// Arrange
		String expected = "Local PV";
		// Assert
		assertEquals(expected, PVType.LOCAL_PV.toString());
	}
	
	@Test
	public final void remote_pv_to_string() {
		// Arrange
		String expected = "Remote PV";
		// Assert
		assertEquals(expected, PVType.REMOTE_PV.toString());
	}
	
	@Test
	public final void variable_local_pv_type_to_string() {
		// Arrange
		PVType pvType = PVType.LOCAL_PV;
		String expected = "Local PV";
		// Assert
		assertEquals(expected, pvType.toString());
	}

}
