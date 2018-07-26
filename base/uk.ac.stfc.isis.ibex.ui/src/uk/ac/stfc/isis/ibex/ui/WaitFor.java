
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

import javax.annotation.PostConstruct;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.alarm.AlarmReloadManager;
import uk.ac.stfc.isis.ibex.ui.dialogs.WaitForDialog;

/**
 * The Class WaitFor which is a handler which will show and hide the wait for the server dialogue.
 */
public class WaitFor {
	
	private WaitForDialog dialog;	
		
	private final Collection<Waiting> waiters = new ArrayList<>();
	
	private IEclipseContext context;
	
	
	/**
	 * Sets the up wait for dialogue.
	 *
	 * @param context the eclipse context
	 */
	@PostConstruct
	public void setUp(IEclipseContext context) {
		this.context = context;
		configure();
	}
	
	/**
	 *  
	 * @return true; this handler can always be executed 
	 */
	@CanExecute 
	public boolean canExecute() {
		return true;
	}
	
	/**
	 * Get the registered extension points and add change listeners
	 */
	private void configure() {
		for (IConfigurationElement element : getRegistered()) {
			try {
				Waiting waiter = (Waiting) element.createExecutableExtension("class");	
				waiters.add(waiter);
				waiter.addPropertyChangeListener("isWaiting", showWaitDialogListener(waiter));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Show wait dialog listener, which will trigger the wait dialogue if the listener changes.
	 *
	 * @param waiter the waiter
	 * @return the property change listener
	 */
	private PropertyChangeListener showWaitDialogListener(final Waiting waiter) {
		return new PropertyChangeListener() {	
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				Display.getDefault().asyncExec(handleWait(waiter));
			}
		};
	}
	
	/**
	 * Create a waiting dialogue and wait until it is closed.
	 */
	@Execute
	private void doWait() {
		if (dialog != null) {
			return;
		}
		dialog = ContextInjectionFactory.make(WaitForDialog.class, context);
		dialog.setBlockOnOpen(true);
		dialog.open(); // open and wait for dialogue to be closed by stop wait.
		
		dialog = null;
	}

	/**
	 * Close the waiting dialogue.
	 */
	private void stopWait() {
		if (dialog != null) {
			dialog.setCursor(SWT.CURSOR_ARROW);
			dialog.close();
        }
        AlarmReloadManager.getInstance().queueDelayedUpdate();
	}
	
	/**
	 * Gets the registered extensions.
	 *
	 * @return the registered
	 */
	private IConfigurationElement[] getRegistered() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		return registry.getConfigurationElementsFor("uk.ac.stfc.isis.ibex.ui.wait");		
	}

	/**
	 * Handle waiting status change in a waiter.
	 *
	 * @param waiter the waiter that has changed
	 * @return the runnable to use to either stop the wait or show the dialogue
	 */
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
