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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.epics.writing.Writer;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlSetter;

// A lot of unchecked type conversions for mocking purposes
@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class EditableRunControlSettingTest {
    String blockName = "blockname";
    
	@Test
	public void set_low_limit() throws IOException {
		// Arrange

	    // Mock writer with stub method for write
		Writable<Double> mockWritable = mock(Writable.class);

		// Mock run-control server
		RunControlServer mockRunControlServer = mock(RunControlServer.class);
		when(mockRunControlServer.blockRunControlLowLimitSetter(blockName)).thenReturn(mockWritable);

		// Object we are really testing
		RunControlSetter setting = new RunControlSetter(blockName, mockRunControlServer);

		// Act
		setting.setLowLimit(0.);

		// Assert
		// The writer's write method is called with the correct value
		verify(mockWritable, times(1)).uncheckedWrite(0.);
	}
	
	@Test
	public void set_high_limit() throws IOException {
		// Arrange

		// Mock writer with stub method for write
		Writable<Double> mockWritable = mock(Writable.class);

		// Mock run-control server
		RunControlServer mockRunControlServer = mock(RunControlServer.class);
		when(mockRunControlServer.blockRunControlHighLimitSetter(blockName)).thenReturn(mockWritable);

		// Object we are really testing
		RunControlSetter setting = new RunControlSetter(blockName, mockRunControlServer);

		// Act
		setting.setHighLimit(100.);

		// Assert
		// The writer's write method is called with the correct value
		verify(mockWritable, times(1)).uncheckedWrite(100.);
	}
	
	@Test
	public void set_enabled_true() throws IOException {
		// Arrange

		// Mock writer with stub method for write
		Writable<String> mockWritable = mock(Writable.class);

		// Mock run-control server
		RunControlServer mockRunControlServer = mock(RunControlServer.class);
		when(mockRunControlServer.blockRunControlEnabledSetter(blockName)).thenReturn(mockWritable);

		// Object we are really testing
		RunControlSetter setting = new RunControlSetter(blockName, mockRunControlServer);

		// Act
		setting.setEnabled(true);

		// Assert
		// The writer's write method is called with the correct value
		verify(mockWritable, times(1)).uncheckedWrite("YES");
	}
	
	@Test
	public void set_enabled_false() throws IOException {
		// Arrange

		// Mock writer with stub method for write
		Writable<String> mockWriter = mock(Writable.class);

		// Mock run-control server
		RunControlServer mockRunControlServer = mock(RunControlServer.class);
		when(mockRunControlServer.blockRunControlEnabledSetter(blockName)).thenReturn(mockWriter);

		// Object we are really testing
		RunControlSetter setting = new RunControlSetter(blockName, mockRunControlServer);

		// Act
		setting.setEnabled(false);

		// Assert
		// The writer's write method is called with the correct value
		verify(mockWriter, times(1)).uncheckedWrite("NO");
	}
}
