
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

package uk.ac.stfc.isis.ibex.dashboard;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * The bundle activator for the back end implementation of the dashboard.
 */
public class Dashboard implements BundleActivator {

    private static Dashboard instance;
	private static BundleContext context;

    /**
     * @return The singleton instance of the dashboard.
     */
    public static Dashboard getInstance() { 
    	return instance; 
    }
	
	private final DashboardObservables observables;
	
    /**
     * Constructor for the dashboard, creates the singleton and the observables.
     */
	public Dashboard() {
		instance = this;
        observables = new DashboardObservables();
	}
 
    /**
     * Get the observables for the dashboard.
     * 
     * @return The observables for the dashboard.
     */
	public DashboardObservables observables() {
		return observables;
	}
	
    /**
     * Get the context for the bundle.
     * 
     * @return The context for the bundle.
     */
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext bundleContext) throws Exception {
		Dashboard.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext bundleContext) throws Exception {
		Dashboard.context = null;
		observables.close();
	}

}
