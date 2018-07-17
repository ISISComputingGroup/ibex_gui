
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

package uk.ac.stfc.isis.ibex.configserver;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayConfiguration;
import uk.ac.stfc.isis.ibex.configserver.internal.ConfigEditing;
import uk.ac.stfc.isis.ibex.configserver.internal.LoggingConfigurationObserver;
import uk.ac.stfc.isis.ibex.configserver.json.JsonConverters;
import uk.ac.stfc.isis.ibex.configserver.recent.RecentConfigList;
import uk.ac.stfc.isis.ibex.epics.observing.LoggingObserver;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlActivator;
import uk.ac.stfc.isis.ibex.runcontrol.RunControlServer;

/**
 * The initial BundleActivator for the configserver plugin. <br>
 * This is the singleton that should be used to access configuration information from across the GUI.
 */
public class Configurations extends Closer implements BundleActivator {
    /** The associated logger. */
	public static final Logger LOG = IsisLog.getLogger(Configurations.class);

	private static Configurations instance;
	private static BundleContext context;
	
	private final Displaying displaying;
	private final Editing editing;
	private final IocControl iocControl;
	private final RecentConfigList recent;

	private final ConfigServerVariables variables;
	private final ConfigServer server;
	private final RunControlServer runcontrol;
	private final Collection<Subscription> loggingSubscriptions = new ArrayList<>();
	
	/**
	 * The default constructor that is called when the plugin is first loaded.
	 */
	public Configurations() {
		instance = this;
		
		recent = new RecentConfigList();		
        variables = registerForClose(new ConfigServerVariables(new JsonConverters()));
		server = registerForClose(new ConfigServer(variables));
		runcontrol = RunControlActivator.getInstance().getServer();
		
		displaying = registerForClose(new DisplayConfiguration(variables.currentConfig, server, runcontrol));	
		editing = registerForClose(new ConfigEditing(server));

		iocControl = new IocControl(server);
		addLogging();
	}
	
    /**
     * @return this instance of the class
     */
	public static Configurations getInstance() {
		return instance;
	}
	
    /**
     * @return the context
     */
	public static BundleContext getContext() {
		return context;
	}

    /**
     * @return the variables for the configuration server instance
     */
	public ConfigServerVariables variables() {
		return variables;
	}
	
    /**
     * @return the configuration server instance
     */
	public ConfigServer server() {
		return server;
	}
	
    /**
     * @return the configuration information for displaying
     */
	public Displaying display() {
		return displaying;
	}

    /**
     * @return the configuration information for editing
     */
	public Editing edit() {
		return editing;
	}
	
    /**
     * @return IOC information
     */
	public IocControl iocControl() {
		return iocControl;
	}
	
    /**
     * Returns the names and time stamps of recently used configurations.
     * 
     * @return the names and time stamps of recently used configurations.
     */
	public List<String> recent() {
		return recent.get();
	}
	
	/**
	 * Returns the time stamps at which recently used configurations were last loaded.
	 * 
     * @return the time stamps at which recently used configurations were last loaded.
     */
    public List<String> recentTimestamps() {
        return recent.getTimestamps();
    }
    
    /**
     * Returns the names of recently used configurations.
     * 
     * @return the names of recently used configurations.
     */
    public List<String> recentNames() {
        return recent.getNames();
    }

    /**
     * Add a configuration and time stamp of when it was last used to the "recently used" list.
     * 
     * @param configName the name to add
     */
	public void addRecent(String configName) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		recent.add(configName + " " + sdf.format(timestamp));
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext bundleContext) throws Exception {
		Configurations.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext bundleContext) throws Exception {
		Configurations.context = null;

		for (Subscription subscription : loggingSubscriptions) {
			subscription.removeObserver();
		}
		
		close();
	}

	private void addLogging() {
		loggingSubscriptions.add(variables.currentConfig.addObserver(new LoggingConfigurationObserver(LOG, "Current config")));
		loggingSubscriptions.add(variables.serverStatus.addObserver(new LoggingObserver<ServerStatus>(LOG, "Server status")));
	}
}
