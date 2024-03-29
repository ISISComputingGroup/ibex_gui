
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

import java.io.IOException;
import java.util.Objects;

import org.apache.logging.log4j.Logger;
import org.csstudio.opibuilder.OPIBuilderPlugin;
import org.csstudio.opibuilder.script.RhinoWithFastPathScriptStore;
import org.csstudio.opibuilder.scriptUtil.PVUtil;
import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * The activator for the plug-in.
 */
public class Opi implements BundleActivator {
	
	private static BundleContext context;

	private static Opi instance;
	
	private static OpiProvider opiProvider = new OpiProvider();
	
	private static DescriptionsProvider descProvider = new DescriptionsProvider();
	
	private static final java.util.logging.Logger CSS_LOGGER = OPIBuilderPlugin.getLogger();
	private static final Logger LOG = IsisLog.getLogger(Opi.class);
	
	static {
		try {
			// Set a java system property pointing to the root of the opi /resources folder. This is used
			// in some CSS scripts to dynamically add imports directories, as __file__ doesn't work in jython.
			final var resourcesPath = FileLocator.resolve(Opi.class.getResource("/resources")).getPath();
			System.setProperty("ibex.opis.resources_directory", resourcesPath);
			LOG.info("Loading OPIs from: " + resourcesPath);
		} catch (IOException e) {
			LoggerUtils.logErrorWithStackTrace(LOG, e.getMessage(), e);
		}
	}
	
	/**
	 * Create the singleton instance of this class.
	 */
	public Opi() {
		instance = this;
	}
	
	/**
	 * Gets the singleton instance of this class.
	 * @return the instance
	 */
	public static Opi getDefault() {
		return instance;
	}
	
	/**
	 * Get the OPI provider.
	 * @return the provider
	 */
	public OpiProvider opiProvider() {
		return opiProvider;
	}
	
	/**
	 * Get the opi descriptions provider.
	 * @return the provider
	 */
	public DescriptionsProvider descriptionsProvider() {
		return descProvider;
	}
	
	/**
	 * Gets the eclipse bundle context.
	 * @return the context
	 */
	static BundleContext getContext() {
		return context;
	}

	/**
	 * Starts this plugin.
	 * 
	 * @param bundleContext the eclipse bundle context
	 * @throws Exception on failure
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Opi.context = bundleContext;
		
		IsisLog.hookJavaLogger(CSS_LOGGER);
		
		// Change this to true to log all scripts which use the slow (JS interpreter) path
		RhinoWithFastPathScriptStore.setLogScriptsUsingJS(false);
		
		addFastPathHandlers();
	}
	
	@SuppressWarnings("checkstyle:magicnumber")
	private static void addFastPathHandlers() {
		
		// Some handlers called frequently by reflectometry OPIs.
		RhinoWithFastPathScriptStore.addFastPathHandler("pv1==0&&pv0==0", 
				pvs -> PVUtil.getDouble(pvs[0]) == 0.0 && PVUtil.getDouble(pvs[1]) == 0.0);
		
		RhinoWithFastPathScriptStore.addFastPathHandler("pv0==1 && pv1==0", 
				pvs -> PVUtil.getDouble(pvs[0]) == 1.0 && PVUtil.getDouble(pvs[1]) == 0.0);
		
		RhinoWithFastPathScriptStore.addFastPathHandler("pvStr0==\"North\" || pvStr0==\"South\"", 
				pvs -> (Objects.equals(PVUtil.getString(pvs[0]), "North")) 
						|| (Objects.equals(PVUtil.getString(pvs[0]), "South")));
				
		RhinoWithFastPathScriptStore.addFastPathHandler("pvStr0!=\"North\" && pvStr0!=\"South\"", 
				pvs -> (!Objects.equals(PVUtil.getString(pvs[0]), "North")) 
						&&  (!Objects.equals(PVUtil.getString(pvs[0]), "South")));
		
		// Some handlers used frequently by RIKEN OPIs.
		RhinoWithFastPathScriptStore.addFastPathHandler("pv0==1 || pv0==3",
				pvs -> PVUtil.getDouble(pvs[0]) == 1.0 || PVUtil.getDouble(pvs[0]) == 3.0);
		
		RhinoWithFastPathScriptStore.addFastPathHandler("pv0==2",
				pvs -> PVUtil.getDouble(pvs[0]) == 2.0);
		
		RhinoWithFastPathScriptStore.addFastPathHandler("pv0==4",
				pvs -> PVUtil.getDouble(pvs[0]) == 4.0);
		
	}

	/**
	 * Stops this plugin.
	 * 
	 * @param bundleContext the eclipse bundle context
	 * @throws Exception on failure
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Opi.context = null;
	}

}
