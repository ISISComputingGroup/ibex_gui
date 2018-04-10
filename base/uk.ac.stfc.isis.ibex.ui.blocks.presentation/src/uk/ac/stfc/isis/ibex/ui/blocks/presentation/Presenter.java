
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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * This plugin creates an extension point for other plugins to hook into if they have a method for displaying historic PV data.
 */
public class Presenter extends Plugin {	
    private static Presenter instance;
	private PVHistoryPresenter pvHistoryPresenter;

	/**
	 * Get the singleton instance of this class.
	 * @return The instance of this class.
	 */
    public static Presenter getInstance() {
    	return instance; 
    }

    /**
     * Default constructor for the plugin.
     */
	public Presenter() {
		instance = this;
	}
	
	/**
	 * Get the PV history presenter.
	 * @return The PV history presenter.
	 */
	public PVHistoryPresenter pvHistoryPresenter() {
		return pvHistoryPresenter;
	}
	
	/**
	 * Starts the plugin and finds all presenters that are extending the extension point.
	 * @param bundleContext the context for this plugin
	 */
	public void start(BundleContext bundleContext) {
		pvHistoryPresenter = locatePVHistoryPresenter();
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

