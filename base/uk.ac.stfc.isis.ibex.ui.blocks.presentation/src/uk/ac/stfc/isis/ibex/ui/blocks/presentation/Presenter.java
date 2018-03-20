
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

package uk.ac.stfc.isis.ibex.ui.blocks.presentation;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.logger.IsisLog;

public class Presenter extends Plugin {

	public static final Logger LOG = IsisLog.getLogger("Blocks");
	
	private static BundleContext context;

    private static Presenter instance;
	private PVHistoryPresenter pvHistoryPresenter;

    public static Presenter getInstance() { 
    	return instance; 
    }
	
	static BundleContext getContext() {
		return context;
	}

	public Presenter() {
		instance = this;
	}
	
	public PVHistoryPresenter pvHistoryPresenter() {
		return pvHistoryPresenter;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Presenter.context = bundleContext;
		
		pvHistoryPresenter = locatePVHistoryPresenter();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Presenter.context = null;
	}
	
	private PVHistoryPresenter locatePVHistoryPresenter() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry.getConfigurationElementsFor("uk.ac.stfc.isis.ibex.ui.blocks.presentation");
		
		if (elements.length == 0) {
			return new NullPVHistoryPresenter();
		}
		
		try {
			final Object obj = elements[0].createExecutableExtension("class");
			return (PVHistoryPresenter) obj;
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return new NullPVHistoryPresenter();
	}

}

