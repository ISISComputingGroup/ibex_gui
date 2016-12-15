
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

package uk.ac.stfc.isis.ibex.beamstatus;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.beamstatus.internal.BeamStatusObservables;

/**
 * The plugin entry point for the back end of the beam status display.
 */
public class BeamStatus extends Plugin {

    private static BeamStatus instance;
	private static BundleContext context;
	
	private final Observables observables;	
	private final BeamStatusObservables status;
	
    /**
     * Default constructor for the singleton. Will be called by eclipse when
     * plugin is started.
     */
	public BeamStatus() {
		instance = this; 
		status = new BeamStatusObservables();
		observables = new Observables(status);
	}
    
    /**
     * @return The instance of this singleton.
     */
    public static BeamStatus getInstance() { 
    	return instance; 
    }

    /**
     * @return Observables for the PVs that contain information on the beam
     *         status.
     */
    public Observables observables() {
    	return observables;
    }
	
    /**
     * @return The BundleContext for this plugin.
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
		BeamStatus.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext bundleContext) throws Exception {
		BeamStatus.context = null;
		status.close();
		observables.close();
	}
}


