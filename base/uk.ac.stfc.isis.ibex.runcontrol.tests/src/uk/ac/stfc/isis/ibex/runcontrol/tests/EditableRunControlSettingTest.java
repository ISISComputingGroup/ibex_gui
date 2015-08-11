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

import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.writing.Writer;
import uk.ac.stfc.isis.ibex.runcontrol.EditableRunControlSetting;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;

// A lot of unchecked type conversions for mocking purposes
@SuppressWarnings({ "unchecked", "checkstyle:methodname" })
public class EditableRunControlSettingTest {

	@Test
	public void set_low_limit() {
		// Arrange
		String blockName = "blockname";

		// Mock writer with stub method for write
		Writer<String> mockWriter = mock(Writer.class);

		// Mock run-control server
		RunControlServer mockRunControlServer = mock(RunControlServer.class);
		when(mockRunControlServer.blockRunControlLowLimitSetter(blockName)).thenReturn(mockWriter);

		// Object we are really testing
		EditableRunControlSetting setting = new EditableRunControlSetting(blockName, mockRunControlServer);

		// Act
		setting.setLowLimit("0");

		// Assert
		// The writer's write method is called with the correct value
		verify(mockWriter, times(1)).write("0");
	}
	
	@Test
	public void set_high_limit() {
		// Arrange
		String blockName = "blockname";

		// Mock writer with stub method for write
		Writer<String> mockWriter = mock(Writer.class);

		// Mock run-control server
		RunControlServer mockRunControlServer = mock(RunControlServer.class);
		when(mockRunControlServer.blockRunControlHighLimitSetter(blockName)).thenReturn(mockWriter);

		// Object we are really testing
		EditableRunControlSetting setting = new EditableRunControlSetting(blockName, mockRunControlServer);

		// Act
		setting.setHighLimit("100");

		// Assert
		// The writer's write method is called with the correct value
		verify(mockWriter, times(1)).write("100");
	}
	
	@Test
	public void set_enabled_true() {
		// Arrange
		String blockName = "blockname";

		// Mock writer with stub method for write
		Writer<String> mockWriter = mock(Writer.class);

		// Mock run-control server
		RunControlServer mockRunControlServer = mock(RunControlServer.class);
		when(mockRunControlServer.blockRunControlEnabledSetter(blockName)).thenReturn(mockWriter);

		// Object we are really testing
		EditableRunControlSetting setting = new EditableRunControlSetting(blockName, mockRunControlServer);

		// Act
		setting.setEnabled(true);

		// Assert
		// The writer's write method is called with the correct value
		verify(mockWriter, times(1)).write("YES");
	}
	
	@Test
	public void set_enabled_false() {
		// Arrange
		String blockName = "blockname";

		// Mock writer with stub method for write
		Writer<String> mockWriter = mock(Writer.class);

		// Mock run-control server
		RunControlServer mockRunControlServer = mock(RunControlServer.class);
		when(mockRunControlServer.blockRunControlEnabledSetter(blockName)).thenReturn(mockWriter);

		// Object we are really testing
		EditableRunControlSetting setting = new EditableRunControlSetting(blockName, mockRunControlServer);

		// Act
		setting.setEnabled(false);

		// Assert
		// The writer's write method is called with the correct value
		verify(mockWriter, times(1)).write("NO");
	}
}
