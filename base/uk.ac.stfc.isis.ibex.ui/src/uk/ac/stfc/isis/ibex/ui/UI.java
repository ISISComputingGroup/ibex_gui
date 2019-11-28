
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class UI extends AbstractUIPlugin implements IStartup {

    /**
     * The plug-in ID.
     */
	public static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.ui"; //$NON-NLS-1$

	// The shared instance
	private static UI plugin;

	@SuppressWarnings("unused")
	private WaitFor waiting;
	
	/**
     * The constructor.
     */
	public UI() {
	}
	
	private final String switchToOrFromIOCLogProperty = "switchToOrFromIOCLog";
	
	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
	
	/**
	 * Add a property change support listener for stopWait changes.
	 * 
	 * @param listener To listen for property changes.
	 */
	public void addSwitchIOCLogPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(switchToOrFromIOCLogProperty, listener);
    }

	/**
	 * Remove a property change support listener for stopWait changes.
	 * 
	 * @param listener That will no longer listen to property support changes.
	 */
    public void removeSwitchIOCLogPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(switchToOrFromIOCLogProperty, listener);
    }
    
    /**
     * Called when PerspectiveSwitcher switches to or from the IOC log.
     */
    public void switchIOCLog() {
    	propertyChangeSupport.firePropertyChange(switchToOrFromIOCLogProperty, null, null);
    }
    
    private final String stopWaitProperty = "stopWait";
	
	/**
	 * Add a property change support listener for stopWait changes.
	 * 
	 * @param listener To listen for property changes.
	 */
	public void addStopWaitPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(stopWaitProperty, listener);
    }

	/**
	 * Remove a property change support listener for stopWait changes.
	 * 
	 * @param listener That will no longer listen to property support changes.
	 */
    public void removeStopWaitPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(stopWaitProperty, listener);
    }
    
    /**
     * Called when WaitFor stopWait occurs.
     */
    public void stopWait() {
    	propertyChangeSupport.firePropertyChange(stopWaitProperty, null, null);
    }
	
	/**
	 * Switch perspectives.
	 *
	 * @param perspectiveID the perspective ID to switch to
	 */
	public void switchPerspective(final String perspectiveID) {
		Display.getDefault().syncExec(new Runnable() {
		    @Override
		    public void run() {
				IWorkbench workbench = PlatformUI.getWorkbench();	   
				IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
				PerspectiveSwitcher switcher = new PerspectiveSwitcher(workbench, workbenchWindow);
				switcher.switchTo(perspectiveID).run();		    
			}
		});
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
    public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
     * Returns the shared instance.
     *
     * @return the shared instance
     */
	public static UI getDefault() {
		return plugin;
	}

	/**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path.
     *
     * @param path
     *            the path
     * @return the image descriptor
     */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void earlyStartup() {
	}

}
