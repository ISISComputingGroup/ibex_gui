
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

package uk.ac.stfc.isis.ibex.e4.app.statusbar;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.configserver.Configurations;
import uk.ac.stfc.isis.ibex.configserver.displaying.DisplayConfiguration;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Subscription;


public class StatusBar {
	
	private Subscription configSubscription;
	
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
	
	private Label lbl;
	
	@PostConstruct
	public void createControls(Composite parent) {		
		final Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		comp.setLayout(new GridLayout(1,false));
		
		lbl = new Label(comp, SWT.LEFT);
		GridData gd_lbl = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_lbl.widthHint = 400;
		lbl.setLayoutData(gd_lbl);
		lbl.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
		subscribeToConfig();
	}

	private void setTitle(final String title, final String description) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {				
		    	lbl.setText("Current configuration: " + title);
		    	lbl.setToolTipText(description);
			}
		});
	}
	
	public void subscribeToConfig() {
		ForwardingObservable<DisplayConfiguration> config = 
				Configurations.getInstance().display().displayCurrentConfig();
		
		configSubscription = config.addObserver(configObserver);
	}

	@PreDestroy
	public void stop() {
		if (configSubscription != null) {
			configSubscription.removeObserver();
		}
		
	};
}
