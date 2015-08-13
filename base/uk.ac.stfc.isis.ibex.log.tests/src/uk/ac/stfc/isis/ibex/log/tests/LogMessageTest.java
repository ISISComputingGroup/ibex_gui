
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

package uk.ac.stfc.isis.ibex.log.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import uk.ac.stfc.isis.ibex.log.message.LogMessage;
import uk.ac.stfc.isis.ibex.log.message.LogMessageFields;
import uk.ac.stfc.isis.ibex.log.message.sql.LogMessageFieldsSql;

/**
 * This class is responsible for ... 
 *
 */
public class LogMessageTest {

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.log.message.LogMessage#setProperty(uk.ac.stfc.isis.ibex.log.message.LogMessageFields, java.lang.String)}.
	 */
	@Test
	public final void testSetPropertyLogMessageFieldsString() {
		// Arrange
		LogMessage mockedLogMessage = mock(LogMessage.class);
		LogMessageFields property = LogMessageFields.SEVERITY;
		String value = "TEST";
		// Act
		mockedLogMessage.setProperty(property, value);
		// Assert
		verify(mockedLogMessage).setProperty(property, value);
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.log.message.LogMessage#getProperty(uk.ac.stfc.isis.ibex.log.message.LogMessageFields)}.
	 */
	@Test
	public final void testGetProperty() {
		// Arrange
		LogMessage testLogMessage = new LogMessage();
		LogMessageFields property = LogMessageFields.SEVERITY;
		String value = "TEST";
		testLogMessage.setProperty(property, value);
		// Act
		String result = testLogMessage.getProperty(property);
		// Assert
		assertEquals(value, result);
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.log.message.LogMessage#getProperties(uk.ac.stfc.isis.ibex.log.message.LogMessageFields[])}.
	 */
	@Test
	public final void testGetProperties() {
		// Arrange
		LogMessage testLogMessage = new LogMessage();
		LogMessageFields property1 = LogMessageFields.SEVERITY;
		LogMessageFields property2 = LogMessageFields.CONTENTS;
		String value = "TEST";
		testLogMessage.setProperty(property1, value);
		LogMessageFields[] properties = {property1, property2};
		// Act
		Object[] reply = testLogMessage.getProperties(properties);
		String result = reply[0].toString();
		// Assert
		assertEquals(value, result);
	}

	/**
	 * Test method for {@link uk.ac.stfc.isis.ibex.log.message.LogMessage#setProperty(uk.ac.stfc.isis.ibex.log.message.sql.LogMessageFieldsSql, java.lang.String)}.
	 */
	@Test
	public final void testSetPropertyLogMessageFieldsSqlString() {
		// Arrange
		LogMessage mockedLogMessage = mock(LogMessage.class);
		LogMessageFieldsSql property = LogMessageFieldsSql.SEVERITY;
		String value = "TEST";
		// Act
		mockedLogMessage.setProperty(property, value);
		// Assert
		verify(mockedLogMessage).setProperty(property, value);
	}

}
