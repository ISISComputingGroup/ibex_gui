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

package uk.ac.stfc.isis.ibex.runcontrol.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.runcontrol.internal.RunControlAddresses;

// A lot of unchecked type conversions for mocking purposes
@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class RunControlAddressesTest {
	@Test
	public void get_low_limit_pv() {
		// Arrange
		String blockName = "blockname";
		RunControlAddresses addresses = new RunControlAddresses();
		
		// Act
		String ans = addresses.getLowLimitPv(blockName);

		// Assert
		assertEquals("CS:SB:blockname:RC:LOW", ans);
	}
	
	@Test
	public void get_high_limit_pv() {
		// Arrange
		String blockName = "blockname";
		RunControlAddresses addresses = new RunControlAddresses();
		
		// Act
		String ans = addresses.getHighLimitPv(blockName);

		// Assert
		assertEquals("CS:SB:blockname:RC:HIGH", ans);
	}
	
	@Test
	public void get_enabled_pv() {
		// Arrange
		String blockName = "blockname";
		RunControlAddresses addresses = new RunControlAddresses();
		
		// Act
		String ans = addresses.getEnablePv(blockName);

		// Assert
		assertEquals("CS:SB:blockname:RC:ENABLE", ans);
	}
	
	@Test
	public void get_inrange_pv() {
		// Arrange
		String blockName = "blockname";
		RunControlAddresses addresses = new RunControlAddresses();
		
		// Act
		String ans = addresses.getInRangePv(blockName);

		// Assert
		assertEquals("CS:SB:blockname:RC:INRANGE", ans);
	}
}
