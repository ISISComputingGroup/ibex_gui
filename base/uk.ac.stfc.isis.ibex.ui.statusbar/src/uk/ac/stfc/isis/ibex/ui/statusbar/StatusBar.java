
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

package uk.ac.stfc.isis.ibex.ui.statusbar;

import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayConfiguration;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;


public class StatusBar extends AbstractUIPlugin{
	
	private static StatusBar instance;
	
	private final Display display = Display.getCurrent();

	private Subscription configSubscription;

	public StatusBar() {
		instance = this;
	}
	
	private final BaseObserver<DisplayConfiguration> configObserver = new BaseObserver<DisplayConfiguration>() {
		@Override
		public void onValue(DisplayConfiguration value) {
			setTitle(value.name(), value.description());
		}

		@Override
		public void onError(Exception e) {
			setUnknownConfiguration();
		}

		@Override
		public void onConnectionStatus(boolean isConnected) {
			if (!isConnected) {
				setUnknownConfiguration();
			}
		}
		
		private void setUnknownConfiguration() {
			setTitle("Unknown", "Unable to display the configuration");
		}		
	};	
	
	public static StatusBar getInstance() {
		return instance;
	}
	
	public void subscribeToConfig() {
		ForwardingObservable<DisplayConfiguration> CONFIG = 
				Configurations.getInstance().display().displayCurrentConfig();
		
		configSubscription = CONFIG.addObserver(configObserver);
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		if (configSubscription != null) {
			configSubscription.removeObserver();
		}
		super.stop(context);
	};
	
	private void setTitle(final String title, final String description) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				IWorkbenchPartSite site = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActivePart().getSite();
				IViewSite vSite = (IViewSite) site;
				IStatusLineManager statusLineManager = vSite.getActionBars().getStatusLineManager();
				
		    	StatusLineConfigLabel statusItem = (StatusLineConfigLabel)statusLineManager.find("CurrentConfigTitle");
		    	statusItem.setConfig(title);
		    	statusItem.setToolTip(description);
		    	statusLineManager.update(true);
			}
		});
	}
}
