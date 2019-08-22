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

package uk.ac.stfc.isis.ibex.logger.tests;

import uk.ac.stfc.isis.ibex.logger.LoggerUtils;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.mockito.Mockito;


/**
 * This class is responsible for testing the Logger Utils.
 * 
 */
public class LoggerUtilsTests {

    /**
     * Test method for
     * {@link uk.ac.stfc.isis.ibex.logger.LoggerUtils.logErrorWithStackTrace()}
     * .
     */
    @Test
    public final void testGivenExceptionWithNoCauseThenOneErrorLogged() {
    	Exception exception = new Exception("My message");
    	Logger mockLog = Mockito.mock(Logger.class);
    	
    	LoggerUtils.logErrorWithStackTrace(mockLog, "Starting message", exception);
    	
    	Mockito.verify(mockLog, Mockito.times(1)).error(Mockito.anyString());
    	
    }
    
    /**
     * Test method for
     * {@link uk.ac.stfc.isis.ibex.logger.LoggerUtils.logErrorWithStackTrace()}
     * .
     */
    @Test
    public final void testGivenExceptionWithACauseThenCauseErrorAlsoLogged() {
    	Exception causeException = new Exception("Cause Message");
    	Exception rootException = new Exception("Root message", causeException);
    	
    	Logger mockLog = Mockito.mock(Logger.class);
    	
    	LoggerUtils.logErrorWithStackTrace(mockLog, "Starting message", rootException);
    	
    	Mockito.verify(mockLog, Mockito.times(2)).error(Mockito.anyString());
    	
    }

}
