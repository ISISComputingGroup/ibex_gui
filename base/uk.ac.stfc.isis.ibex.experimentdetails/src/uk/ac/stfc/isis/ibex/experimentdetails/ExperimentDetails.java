
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

package uk.ac.stfc.isis.ibex.experimentdetails;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.experimentdetails.internal.ExperimentDetailsVariables;
import uk.ac.stfc.isis.ibex.experimentdetails.internal.ObservableModel;
import uk.ac.stfc.isis.ibex.instrument.Instrument;

public class ExperimentDetails extends AbstractUIPlugin {

	private static BundleContext context;
	private static ExperimentDetails instance;
	
	private ExperimentDetailsVariables variables; 
	private Model model;

	static BundleContext getContext() {
		return context;
	}
	
	public ExperimentDetails() {
		instance = this;		
		variables = new ExperimentDetailsVariables(Instrument.getInstance().channels()); //prefixes the PV
		model = new ObservableModel(variables);
	}
	
	public static ExperimentDetails getInstance() {
		return instance;
	}
	
	public Model model() {
		return model;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		ExperimentDetails.context = bundleContext;
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		ExperimentDetails.context = null;
		
		variables.close();
	}

}
