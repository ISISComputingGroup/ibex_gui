
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

package uk.ac.stfc.isis.ibex.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.alarm.AlarmReloadManager;
import uk.ac.stfc.isis.ibex.ui.dialogs.WaitForDialog;

public class WaitFor {
	
	private WaitForDialog dialog;	
	private final Display display = Display.getDefault();
	
	private final Collection<Waiting> waiters = new ArrayList<>();
	
	public WaitFor() {
		configure();
	}
	
	private void configure() {
		for (IConfigurationElement element : getRegistered()) {
			try {
				Waiting waiter = extractWaiter(element);	
				waiters.add(waiter);
				waiter.addPropertyChangeListener("isWaiting", showWaitDialog(waiter));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private PropertyChangeListener showWaitDialog(final Waiting waiter) {
		return new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				display.asyncExec(handleWait(waiter));
			}
		};
	}
	
	private void doWait() {
		if (dialog == null) {
			createDialog();
		}
		

		dialog.open();
		dialog.setCursor(SWT.CURSOR_ARROW);
	}

	private void stopWait() {
		if (dialog != null) {
			dialog.close();
        }
        AlarmReloadManager.getInstance().queueDelayedUpdate();
	}
	
	// Must call from the UI thread.
	private void createDialog() {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		dialog = new WaitForDialog(shell);
		dialog.setBlockOnOpen(true);
	}
	
	private IConfigurationElement[] getRegistered() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		return registry.getConfigurationElementsFor("uk.ac.stfc.isis.ibex.ui.wait");		
	}

	private Waiting extractWaiter(IConfigurationElement element) throws CoreException {
		return (Waiting) element.createExecutableExtension("class");
	}
	
	private Runnable handleWait(final Waiting waiter) {
		return new Runnable() {
			@Override
            public void run() {
				boolean isWaiting = waiter.isWaiting();
				if (isWaiting) {
					doWait();
				} else {
					stopWait();
				}						
			}
		};
	}
}
