
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

package uk.ac.stfc.isis.ibex.opis;

import org.csstudio.opibuilder.OPIBuilderPlugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * The activator for the plug-in.
 *
 */
public class Opi implements BundleActivator {

	private static BundleContext context;

	private static Opi instance;
	
	private static OpiProvider opiProvider = new OpiProvider();
	
	private static DescriptionsProvider descProvider = new DescriptionsProvider();
	
	private static final java.util.logging.Logger CSS_LOGGER = OPIBuilderPlugin.getLogger();
	
	public Opi() {
		instance = this;
	}
	
	public static Opi getDefault() {
		return instance;
	}
	
	public OpiProvider opiProvider() {
		return opiProvider;
	}
	
	public DescriptionsProvider descriptionsProvider() {
		return descProvider;
	}
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Opi.context = bundleContext;
		
		IsisLog.hookJavaLogger(CSS_LOGGER);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Opi.context = null;
	}

}
