
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

package uk.ac.stfc.isis.ibex.epics.tests.observing;

import static org.mockito.Mockito.*;

import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.LoggingObserver;

@SuppressWarnings({ "checkstyle:methodname" })
public class LoggingObserverTest {
	
	private static final String ON_VALUE_MESSAGE = " value: ";
	private static final String ON_ERROR_MESSAGE = " error: ";
	private static final String ON_CONNECTION_CHANGED_MESSAGE = " connected: ";
	private static final String ID = "id";
	
	private Logger mockLogger;
	private LoggingObserver<String> loggingObserver;
	
	private TestableObservable<String> testableObservable;
	
	@Before
	public void setUp() {
		// Arrange		
		mockLogger = mock(Logger.class);
		loggingObserver = new LoggingObserver<>(mockLogger, ID);
		
		testableObservable = new TestableObservable<>();
		testableObservable.addObserver(loggingObserver);
	}
	
	@Test
	public void setting_changed_value_change_creates_appropraite_log_messageat_info_level() {
		// Act
		testableObservable.setValue(TestHelpers.STRING_VALUE);
		
		// Assert - The log message is as expected
		verify(mockLogger, times(1)).info(ID + ON_VALUE_MESSAGE + TestHelpers.STRING_VALUE);
	}
	
	@Test
	public void setting_error_on_observable_creates_apporpriate_log_message_at_error_level() {
		// Act
		testableObservable.setError(TestHelpers.EXCEPTION);
		
		// Assert - The log message is as expected
		verify(mockLogger, times(1)).error(ID + ON_ERROR_MESSAGE + TestHelpers.EXCEPTION);
	}
	
	@Test
	public void setting_connection_status_on_observable_creates_appropriate_log_message_at_info_level() {
		// Act
		testableObservable.setConnectionStatus(true);
		
		// Assert - The log message is as expected
		verify(mockLogger, times(1)).info(ID + ON_CONNECTION_CHANGED_MESSAGE + true);
	}
	
	@Test
	public void changing_value_twice_to_same_value_creates_two_log_message() {
		// Act
		testableObservable.setValue(TestHelpers.STRING_VALUE);
		testableObservable.setValue(TestHelpers.STRING_VALUE);
		
		// Assert - The log message is as expected
		verify(mockLogger, times(2)).info(ID + ON_VALUE_MESSAGE + TestHelpers.STRING_VALUE);
	}
	
	@Test
	public void changing_to_null_value_does_not_create_log_message() {
		// Arrange
		verify(mockLogger, times(1)).info(anyString());  // Info called on update() when observer is added
		
		// Act
		testableObservable.setValue(null);
		
		// Assert
		verify(mockLogger, times(1)).info(anyString());  // setValue has not been called again
	}	
}
