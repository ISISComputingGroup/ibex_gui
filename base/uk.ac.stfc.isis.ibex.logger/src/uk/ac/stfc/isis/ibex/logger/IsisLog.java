
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
	
	public static Logger getLogger(String className) {
		return LogManager.getLogger(className);
	}
	
	public static Logger getLogger(Class<?> clazz) {
		return LogManager.getLogger(clazz);
	}

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
	}

	@Override
    public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	
	// Hook all loaded bundles into the log4j framework
	private void hookPluginLoggers(final BundleContext context) {
	    for (Bundle bundle : context.getBundles()) {
	        ILog pluginLogger = Platform.getLog(bundle);
	        pluginLogHooks.add(new LogListener(pluginLogger, LogManager.getLogger(bundle.getSymbolicName())));
	    }
	}

	/**
     * Returns the shared instance.
     *
     * @return the shared instance
     */
	public static IsisLog getDefault() {
		return plugin;
	}
	
	public static RecentLog getLoggerModel() {
		return loggerModel;
	}
}
