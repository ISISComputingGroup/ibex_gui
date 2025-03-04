
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

package uk.ac.stfc.isis.ibex.e4.product;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import org.csstudio.security.authentication.LoginJob;

import uk.ac.stfc.isis.ibex.epics.pvmanager.PVManagerSettings;
import uk.ac.stfc.isis.ibex.ui.JMXServer;

/**
 * This class controls all aspects of the application's execution.
 */
public class Application implements IApplication {
	
	/* 
	 * If you get an error on the following declarations, you need to go (in eclipse) to:
	 * Window -> preferences -> java -> compiler and change workspace
	 * compliance to java 17.
	 * Also ensure that a compatible Eclipse Adoptium JDK version 17+ is 
	 * selected to build with.
	 */
	private sealed interface Java17SealedInterface permits Java17SealedInterfaceImpl { }
	private static non-sealed class Java17SealedInterfaceImpl implements Java17SealedInterface { }

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	@Override
    public Object start(IApplicationContext context) {
		
		// Start a JMX server for remote diagnostics.
		JMXServer.startJMXServer();
		
		// Set up diirt as early as possible in the startup sequence.
		PVManagerSettings.setUp();
		
        // Authenticate user
        LoginJob.forCurrentUser().schedule();
		
		Display display = PlatformUI.createDisplay();
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	@Override
    public void stop() {
		if (!PlatformUI.isWorkbenchRunning()) {
			return;
		}
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			@Override
            public void run() {
				if (!display.isDisposed()) {
					workbench.close();
				}
			}
		});
	}
}
