
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

package uk.ac.stfc.isis.ibex.runcontrol;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.runcontrol.internal.RunControlVariables;

/**
 * The Eclipse plug-in activator.
 *
 */
public class RunControlActivator extends Closer implements BundleActivator {

	private static BundleContext context;
	private static RunControlActivator instance;
	
	private final RunControlVariables variables;
	private final RunControlServer server;
	
	/**
	 * Creates this plugin.
	 */
	public RunControlActivator() {
		instance = this;
        variables = new RunControlVariables();
		server = registerForClose(new RunControlServer(variables));
	}

	/**
	 * Gets the eclipse bundle context.
	 * @return the context
	 */
	static BundleContext getContext() {
		return context;
	}
	
	/**
	 * Gets the singleton instance of this class.
	 * @return the instance
	 */
	public static RunControlActivator getInstance() {
		return instance;
	}
	
	/**
	 * Gets the singleton instance of the runcontrol variables.
	 * @return the instance
	 */
	public RunControlVariables getVariables() {
		return variables;
	}
	
	/**
	 * Gets the singleton instance of the runcontrol server.
	 * @return the instance
	 */
	public RunControlServer getServer() {
		return server;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext bundleContext) throws Exception {
		RunControlActivator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext bundleContext) throws Exception {
		RunControlActivator.context = null;
		close();
	}

}
