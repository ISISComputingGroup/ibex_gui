
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
import org.junit.Test;

import uk.ac.stfc.isis.ibex.epics.observing.LoggingObserver;

public class LoggingObserverTest {
	
	private String onValueMessage = " value: ";
	private String onErrorMessage = " error: ";
	private String onConnectionChangedMessage = " connected: ";
	private String id = "id";
	
	@Test
	public void test_LoggingObserver_on_value() {
		//Arrange
		String value = "value";

		Logger mockLogger = mock(Logger.class);		

		// The LoggingObserver to test
		LoggingObserver<String> loggingObserver = new LoggingObserver<>(mockLogger, id);
		
		// A test observable that has public set methods
		TestableObservable<String> testableObservable = new TestableObservable<>();
		
		//Act
		testableObservable.subscribe(loggingObserver);
		testableObservable.setValue(value);
		
		//Assert
		// The log message is as expected
		verify(mockLogger, times(1)).info(id + onValueMessage + value);
	}
	
	@Test
	public void test_LoggingObserver_on_error() {
		//Arrange
		Exception exception = new Exception();
		
		// The LoggingObserver to test
		Logger mockLogger = mock(Logger.class);		
		LoggingObserver<String> loggingObserver = new LoggingObserver<>(mockLogger, id);
		
		// A test observable that has public set methods
		TestableObservable<String> testableObservable = new TestableObservable<>();
		
		//Act
		testableObservable.subscribe(loggingObserver);
		testableObservable.setError(exception);
		
		//Assert
		// The log message is as expected
		verify(mockLogger, times(1)).error(id + onErrorMessage + exception);
	}
	
	@Test
	public void test_LoggingObserver_on_connection_changed() {
		//Arrange
		
		boolean connectionChanged = true;
		
		// The LoggingObserver to test
		Logger mockLogger = mock(Logger.class);		
		LoggingObserver<String> loggingObserver = new LoggingObserver<>(mockLogger, id);
		
		// A test observable that has public set methods
		TestableObservable<String> testableObservable = new TestableObservable<>();
		
		//Act
		testableObservable.subscribe(loggingObserver);
		testableObservable.setConnectionChanged(connectionChanged);
		
		//Assert
		// The log message is as expected
		verify(mockLogger, times(1)).info(id + onConnectionChangedMessage + connectionChanged);
	}
}
