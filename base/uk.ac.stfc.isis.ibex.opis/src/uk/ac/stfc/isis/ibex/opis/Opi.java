
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

import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.csstudio.opibuilder.OPIBuilderPlugin;
import org.csstudio.opibuilder.script.RhinoWithFastPathScriptStore;
import org.csstudio.opibuilder.script.ScriptStoreFactory;
import org.csstudio.opibuilder.scriptUtil.PVUtil;
import org.eclipse.swt.widgets.Display;
import org.mozilla.javascript.ClassShutter;
import org.mozilla.javascript.Context;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.logger.LoggerUtils;

/**
 * The activator for the plug-in.
 *
 */
public class Opi implements BundleActivator {
	
	private static final Logger LOG = IsisLog.getLogger(Opi.class);

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
	
	/**
	 * Java packages which are allowed to be accessed from JS rules running in CSS.
	 * 
	 * Any java class not in this list will not be allowed to be imported from JS.
	 */
	private static final Set<String> ALLOWED_JS_PACKAGE_NAMES = Set.of(
		"org.csstudio.opibuilder.scriptUtil.ColorFontUtil",
		"org.csstudio.opibuilder.scriptUtil.ConsoleUtil",
		"org.csstudio.opibuilder.scriptUtil.DataUtil",
		"org.csstudio.opibuilder.scriptUtil.FileUtil",
		"org.csstudio.opibuilder.scriptUtil.GUIUtil",
		"org.csstudio.opibuilder.scriptUtil.PVUtil",
		"org.csstudio.opibuilder.scriptUtil.ScriptUtil",
		"org.csstudio.opibuilder.scriptUtil.WidgetUtil"
	);

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Opi.context = bundleContext;
		
		IsisLog.hookJavaLogger(CSS_LOGGER);
		
		// Unfortunately this has to be done on the UI thread as CSS will execute all rules
		// in GUI thread.
		Display.getDefault().asyncExec(() -> {
			try {
				Context ctx = ScriptStoreFactory.getRhinoContext();
				// -1 is interpreted mode - don't need to compile as our JS
				// scripts are tiny.
				ctx.setOptimizationLevel(-1);
						
				// We won't be attaching a debugger to a CSS rule, so no need
				// to generate debug information
				ctx.setGeneratingDebug(false);
				ctx.setGeneratingSource(false);
				
				ctx.setClassShutter(new ClassShutter() {
					/**
					 * A class shutter avoids rhino trying dynamic imports of
				     * a, then a.b, then a.b.c, then a.b.c.ActualClass while doing
				     * a javascript import of a.b.c.ActualClass.
				     * 
				     * Any class not listed in ALLOWED_JS_PACKAGE_NAMES cannot be
				     * used from within rules in javascript.
				     * In practice all of our JS rules use default CSS behaviours,
				     * which just use o.c.o.scriptUtil.* classes.
				     * 
					 * @param name the name js wants to load
					 * @return whether it is a valid name.
					 */
					@Override
					public boolean visibleToScripts(String name) {
						return ALLOWED_JS_PACKAGE_NAMES.contains(name);
					}
				});
				
			} catch (Exception e) {
				LoggerUtils.logErrorWithStackTrace(LOG, "rhino init failed: " + e.getMessage(), e);
			}
		});
		
		addFastPathHandlers();
	}
	
	private static void addFastPathHandlers() {
		
		// Some handlers called frequently by reflectometry OPIs.
		RhinoWithFastPathScriptStore.addFastPathHandler("pv1==0&&pv0==0", 
				pvs -> PVUtil.getDouble(pvs[0]) == 0.0 && PVUtil.getDouble(pvs[1]) == 0.0);
		RhinoWithFastPathScriptStore.addFastPathHandler("pv0==1 && pv1==0", 
				pvs -> PVUtil.getDouble(pvs[0]) == 1.0 && PVUtil.getDouble(pvs[1]) == 0.0);
		RhinoWithFastPathScriptStore.addFastPathHandler("pvStr0==\"North\" || pvStr0==\"South\"", 
				pvs -> PVUtil.getString(pvs[0]) == "North" || PVUtil.getString(pvs[0]) == "South");
		RhinoWithFastPathScriptStore.addFastPathHandler("pvStr0!=\"North\" && pvStr0!=\"South\"", 
				pvs -> PVUtil.getString(pvs[0]) != "North" && PVUtil.getString(pvs[0]) != "South");
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Opi.context = null;
	}

}
