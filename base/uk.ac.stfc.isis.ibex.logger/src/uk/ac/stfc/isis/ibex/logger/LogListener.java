
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

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.logger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Listens to log messages from the Eclipse logging framework and passes
 * them on to log4j.
 *
 */
public class LogListener implements ILogListener {

    private Logger log4jLogger;
   
    /**
     * Creates a new log listener.
     * @param eclipseLogger the eclipse logger to listen to
     * @param log4jLogger the log4j logger to pass messages to
     */
    public LogListener(ILog eclipseLogger, Logger log4jLogger) {
    	this.log4jLogger = log4jLogger;
    	
    	eclipseLogger.addLogListener(this);
    }
   
    @Override
    public void logging(IStatus status, String plugin) {
    	Level level = Level.INFO;
    	switch (status.getSeverity()) {
    		case Status.WARNING:
    			level = Level.WARN;
    			break;
    			
    		case Status.INFO:
    			level = Level.INFO;
    			break;
    			
    		case Status.ERROR:
    			level = Level.ERROR;
    			break;
    			
    		default:
    			level = Level.INFO;
    	}
    	
    	Throwable exception = status.getException();
    	if (exception != null) {
    		String ls = System.lineSeparator();
    		StringBuilder trace = new StringBuilder();
    		for (StackTraceElement tr: exception.getStackTrace()) {
    			trace.append(ls + "\t" + tr.toString());
    		}
    		log4jLogger.error(exception.getMessage() + trace.toString());
    	} else {
    		log4jLogger.log(level, status.getMessage());
    	}
    	
    	
    }
}