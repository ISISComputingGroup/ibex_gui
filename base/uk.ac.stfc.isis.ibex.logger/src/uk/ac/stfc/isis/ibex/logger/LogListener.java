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
   
    public LogListener(ILog eclipseLogger, Logger log4jLogger) {
    	this.log4jLogger = log4jLogger;
    	
    	eclipseLogger.addLogListener(this);
    }
   
    @Override
    public void logging(IStatus status, String plugin) {
    	Level level = Level.INFO;
    	switch(status.getSeverity()) {
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