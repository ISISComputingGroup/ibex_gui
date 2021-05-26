
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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import uk.ac.stfc.isis.ibex.logger.config.LoggingConfiguration;

/**
 * The activator class controls the plug-in life cycle.
 */
public class IsisLog extends AbstractUIPlugin {

    /**
     * The plug-in ID.
     */
	public static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.logger"; //$NON-NLS-1$

	// The shared instance
	private static IsisLog plugin;
	
	// log4j logger
	private static Logger log;
	
	
	private static RecentLog loggerModel;
	
	// Stored so they are not reclaimed by GC
	private final List<LogListener> pluginLogHooks = new ArrayList<LogListener>();
	
	/**
     * The constructor.
     */
	public IsisLog() {
	}
	
	/**
	 * Gets the logger for the provided class name.
	 * @param className the class name
	 * @return the logger
	 */
	public static Logger getLogger(String className) {
		return LogManager.getLogger(className);
	}
	
	/**
	 * Gets the logger for the provided class.
	 * @param clazz the class
	 * @return the logger
	 */
	public static Logger getLogger(Class<?> clazz) {
		return LogManager.getLogger(clazz);
	}

	/**
	 * Called on start of the IsisLog plugin.
	 */
	@Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
		
		plugin = this;
		
		// Create log configuration
		LoggingConfiguration.configure();
		log = LogManager.getLogger(IsisLog.class);
		
		loggerModel = new RecentLog();
	
		log.info("Starting " + PLUGIN_ID);
		
		hookPluginLoggers(context);
		
		java.util.logging.LogManager.getLogManager().reset();
		
		hookJavaLogger(java.util.logging.Logger.getGlobal());
	}

	/**
	 * Called on stop of the IsisLog plugin.
	 */
	@Override
    public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	
	/**
	 * Hook all loaded bundles into the log4j framework.
	 * @param context the eclipse bundle context
	 */
	private void hookPluginLoggers(final BundleContext context) {
	    for (Bundle bundle : context.getBundles()) {
	        ILog pluginLogger = Platform.getLog(bundle);
	        pluginLogHooks.add(new LogListener(pluginLogger, LogManager.getLogger(bundle.getSymbolicName())));
	    }
	}
	
	/**
	 * Hooks into a java.util.logging.Logger and forces it's message to Log4j instead.
     *
     * This is used to remove the logging to the logging that plugins do and to redirect that into the isis logger so
     * that the logging is to file and console.
	 * @param logger the logger to hook into.
	 */
	public static void hookJavaLogger(final java.util.logging.Logger logger) {
		// Remove any existing handlers
		for (var handler : logger.getHandlers()) {
			logger.removeHandler(handler);
		}
		
		logger.setUseParentHandlers(false);
		logger.setFilter(record -> true);
		logger.setLevel(java.util.logging.Level.INFO);
		
		logger.addHandler(new java.util.logging.Handler() {
			@Override
			public void publish(LogRecord record) {
				IsisLog.getLogger(record.getSourceClassName()).log(Level.toLevel(record.getLevel().toString(), Level.INFO), record.getMessage(), record.getThrown());
			}

			@Override
			public void close() throws SecurityException {
				
			}

			@Override
			public void flush() {
				
			}
		});
		
		IsisLog.getLogger(IsisLog.class).info("java.util.logging.Logger logger " + logger.getName() + " hooked successfully (message from log4j).");
		logger.info("java.util.logging.Logger logger " + logger.getName() + " hooked successfully (message from java logger).");
	}

	/**
     * Returns the shared instance.
     *
     * @return the shared instance
     */
	public static IsisLog getDefault() {
		return plugin;
	}
	
	/**
	 * Gets the logger model.
	 * @return the logger model
	 */
	public static RecentLog getLoggerModel() {
		return loggerModel;
	}
}
