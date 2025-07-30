
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.alerts.AlertsActivator;
import uk.ac.stfc.isis.ibex.alerts.AlertsServer;
import uk.ac.stfc.isis.ibex.configserver.configuration.ConfigInfo;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayConfiguration;
import uk.ac.stfc.isis.ibex.configserver.internal.ConfigEditing;
import uk.ac.stfc.isis.ibex.configserver.internal.LoggingConfigurationObserver;
import uk.ac.stfc.isis.ibex.configserver.json.JsonConverters;
import uk.ac.stfc.isis.ibex.configserver.recent.RecentConfigList;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
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
	private final UpdatedObservableAdapter<HashMap<String, ArrayList<ArrayList<String>>>> moxaMappings;
	private final RecentConfigList recent;

	private final ConfigServerVariables variables;
	private final ConfigServer server;
	private final RunControlServer runcontrol;
	private final Collection<Subscription> loggingSubscriptions = new ArrayList<>();
	private final AlertsServer alertsServer;
	
	/**
	 * The default constructor that is called when the plugin is first loaded.
	 */
	public Configurations() {
		instance = this;
		
		recent = new RecentConfigList();		
        variables = registerForClose(new ConfigServerVariables(new JsonConverters()));
		server = registerForClose(new ConfigServer(variables));
		runcontrol = RunControlActivator.getInstance().getServer();
		
		alertsServer = AlertsActivator.getInstance().getServer();
		displaying = registerForClose(new DisplayConfiguration(variables.currentConfig, server, runcontrol, alertsServer));	
		editing = registerForClose(new ConfigEditing(server));

		iocControl = new IocControl(server);
		moxaMappings = new UpdatedObservableAdapter<>(server.moxaMappings());
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
	 * @return Moxa mapping information
	 */
	public UpdatedObservableAdapter<HashMap<String, ArrayList<ArrayList<String>>>> moxaMappings() { 
		return moxaMappings;
	}
	/**
     * Returns the names of recently used configurations without that of the current configuration.
     * 
     * @param configsInServer
     *                 The collection of information on the configurations in the server.
     * @return 
     *                  The names of recently used configurations without that of the current configuration.
     */
    public List<String> getRecentlyLoadedConfigurations(Collection<ConfigInfo> configsInServer) {
        return recent.getNamesOfRecentlyLoadedConfigs(configsInServer);
    }

    /**
     * Returns the time stamp of when recently used configurations were last modified without that of the current configuration.
     * 
     * @param configsInServer
     *                 The collection of information on the configurations in the server.
     * @return 
     *                 The names of recently used configurations without that of the current configuration.
     */
    public List<String> getLastModifiedTimestampsOfRecentlyLoadedConfigurations(Collection<ConfigInfo> configsInServer) {
        return recent.getLastModifiedTimestamps(configsInServer);
    }

    /**
     * Add a configuration to the "recently used" list.
     * 
     * @param configName the name to add
     */
	public void addNameToRecentlyLoadedConfigList(String configName) {
		recent.add(configName);
	}
	
	/**
     * Removes a configuration from the "recently used" list.
     * 
     * @param configName the name to add
     */
    public void removeNameFromRecentlyLoadedConfigList(String configName) {
        recent.remove(configName);
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
			subscription.cancelSubscription();
		}
		
		close();
	}

	private void addLogging() {
		loggingSubscriptions.add(variables.currentConfig.subscribe(new LoggingConfigurationObserver(LOG, "Current config")));
		loggingSubscriptions.add(variables.serverStatus.subscribe(new LoggingObserver<ServerStatus>(LOG, "Server status")));
		loggingSubscriptions.add(variables.moxaMappings.subscribe(new LoggingObserver<HashMap<String, ArrayList<ArrayList<String>>>>(LOG, "Moxa status", Level.DEBUG)));
		}
}
