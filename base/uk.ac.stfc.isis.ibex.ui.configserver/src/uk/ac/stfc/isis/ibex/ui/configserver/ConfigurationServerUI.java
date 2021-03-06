
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

package uk.ac.stfc.isis.ibex.ui.configserver;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.configserver.Configurations;

/**
 * Controls the life cycle of the configuration server UI plug-in.
 */
public class ConfigurationServerUI implements BundleActivator {

    /** The plug-in ID. */
	public static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.ui.configserver"; //$NON-NLS-1$

    /** shared instance of the plug in. */
	private static ConfigurationServerUI plugin;
	
    /** shared configuration view models. */
    private ConfigurationViewModels configurationViewModels;
	
	/**
     * Constructor.
     */
    public ConfigurationServerUI() {
	}

    /**
     * Get the configuration view models.
     * 
     * @return the configuration view models instance
     */
    public ConfigurationViewModels configurationViewModels() {
        if (configurationViewModels == null) {
            configurationViewModels = new ConfigurationViewModels();
            configurationViewModels.bind(Configurations.getInstance().edit());
        }

        return configurationViewModels;
    }

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext context) throws Exception {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext context) throws Exception {
		plugin = null;
	}

	/**
     * Returns the shared plug-in instance.
     * 
     * @return the shared plug-in instance
     */
    public static ConfigurationServerUI getDefault() {
		return plugin;
	}

}
