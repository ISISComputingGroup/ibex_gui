
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

package uk.ac.stfc.isis.ibex.ui.dae;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.dae.Dae;


/**
 * The activator class controls the plug-in life cycle.
 */
public class DaeUI extends AbstractUIPlugin {

    /**
     * The plug-in ID.
     */
	public static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.ui.dae"; //$NON-NLS-1$

	// The shared instance
	private static DaeUI plugin;
	
	private DaeViewModel viewModel;
	
	/**
     * The constructor.
     */
	public DaeUI() {
	}

    /**
     * Get the View Model for the DAE. Creates a new one if no view model yet
     * exists.
     * 
     * @return The view model
     */
	public DaeViewModel viewModel() {
		if (viewModel == null) {
			viewModel = new DaeViewModel();
			viewModel.bind(Dae.getInstance().model());
		}
		
		return viewModel;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
     * Returns the shared instance.
     *
     * @return the shared instance
     */
	public static DaeUI getDefault() {
		return plugin;
	}

}
