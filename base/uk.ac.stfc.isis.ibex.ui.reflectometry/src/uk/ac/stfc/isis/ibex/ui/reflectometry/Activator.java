
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

package uk.ac.stfc.isis.ibex.ui.reflectometry;

import java.util.Optional;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.managermode.ManagerModeModel;
import uk.ac.stfc.isis.ibex.managermode.ManagerModeObservable;
import uk.ac.stfc.isis.ibex.managermode.ManagerModeObserver;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {

    /**
     * The plug-in ID.
     */
    public static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.ui.reflectometry"; //$NON-NLS-1$

    /**
     * The shared instance.
     */
    private static Activator plugin;
    
    private static IWorkbenchPage current_page;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
    @Override
    public void start(BundleContext context) throws Exception {
		super.start(context);
		ManagerModeObservable managerModePv = ManagerModeModel.getInstance().getManagerModeObservable();
		plugin = this;
		current_page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}

    /**
     * Add observer to the observable.
     */
    private void addObserver(ManagerModeObservable managerModePv) {

		Optional<ManagerModeObserver> managerModeObservable = Optional
				.of(new ManagerModeObserver(managerModePv.observable) {

			@Override
			protected void setManagerMode(Boolean value) {

				if(value){
					try {
						current_page.showView(ReflectometryRedefineMotorsTargetView.ID);
					} catch (PartInitException e) {
						e.printStackTrace();
//			            LOG.catching(e);
					}
				} else {
					current_page.hideView(current_page.findView(ReflectometryRedefineMotorsTargetView.ID));
				}
			}

			@Override
			protected void setUnknown() {
				current_page.hideView(current_page.findView(ReflectometryRedefineMotorsTargetView.ID));
			}

		});
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
	public static Activator getDefault() {
		return plugin;
	}

    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path.
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}