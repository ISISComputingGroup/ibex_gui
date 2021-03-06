
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

package uk.ac.stfc.isis.ibex.dae;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.dae.detectordiagnostics.DetectorDiagnosticsModel;

/**
 * Provides and sets information about the Data Acquisition Electronics.
 */
public class Dae extends Plugin {

    /**
     * The perspective ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.dae.perspective"; //$NON-NLS-1$

    private static Dae instance;
	private static BundleContext context;

	/**
	 * Gets the singleton instance of this class.
	 * 
	 * @return the singleton instance of this class
	 */
    public static Dae getInstance() { 
    	return instance; 
    }

    private final DaeModel model;
	private final DaeWritables writables;
	private final DaeObservables observables;

    /**
     * Default constructor.
     */
    public Dae() {
		instance = this;
        writables = new DaeWritables();
        observables = new DaeObservables();
		model = new DaeModel(writables, observables);
	}
    
    /**
     * Gets the model used by the DAE.
     * 
     * @return the model
     */
	public IDae model() {
		return model;
	}
	
	/**
	 * Gets the observables used by the DAE.
	 * 
	 * @return the observables
	 */
	public DaeObservables observables() {
		return observables;
	}
	
	/**
	 * Gets the bundle context associated with this plugin.
	 * 
	 * @return the bundle context
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
		Dae.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext bundleContext) throws Exception {
		Dae.context = null;
		model.close();
        DetectorDiagnosticsModel.closeInstance();
	}
}
