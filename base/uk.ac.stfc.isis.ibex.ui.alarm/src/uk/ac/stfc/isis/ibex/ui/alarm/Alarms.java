
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

package uk.ac.stfc.isis.ibex.ui.alarm;

import org.apache.logging.log4j.Logger;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.instrument.InstrumentInfo;
import uk.ac.stfc.isis.ibex.instrument.InstrumentInfoReceiver;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * The activator class controls the plug-in life cycle, this plugin shows the
 * alarm perspective.
 */
public class Alarms extends AbstractUIPlugin implements InstrumentInfoReceiver {

    private static final Logger LOG = IsisLog.getLogger(Alarms.class);
    
    /**
     * The plug-in ID.
     */
	public static final String PLUGIN_ID = "uk.ac.stfc.isis.ibex.ui.test"; //$NON-NLS-1$

	// The shared instance
	private static Alarms plugin;

    /**
     * Store whether after instrument switch the alarm perspective should be
     * reopened.
     */
    private boolean reopenalarmPerspective = false;
		
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
	public static Alarms getDefault() {
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

    @Override
    public void setInstrument(InstrumentInfo instrument) {
        // nothing to do on set instrument
    }


    /**
     * Before the instrument changes close the alarm perspective so that it
     * releases it alarm model.
     * 
     * @param instrument instrument to switch to
     */
    @Override
    public void preSetInstrument(InstrumentInfo instrument) {
        reopenalarmPerspective = false;
        IPerspectiveDescriptor descriptor =
                PlatformUI.getWorkbench().getPerspectiveRegistry().findPerspectiveWithId(AlarmPerspective.ID);
        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (activePage != null) {
            IPerspectiveDescriptor alarmPerspective = PlatformUI.getWorkbench().getPerspectiveRegistry()
                    .findPerspectiveWithId(uk.ac.stfc.isis.ibex.ui.alarm.AlarmPerspective.ID);
            reopenalarmPerspective = (activePage.getPerspective() == alarmPerspective);
            activePage.closePerspective(descriptor, true, true);
        }
    }

    @Override
    public void postSetInstrument(InstrumentInfo instrument) {
        if (!reopenalarmPerspective) {
            return;
        }
        try {
            PlatformUI.getWorkbench().showPerspective(uk.ac.stfc.isis.ibex.ui.alarm.AlarmPerspective.ID,
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow());
        } catch (WorkbenchException ex) {
            ex.printStackTrace();
            LOG.error("Error reopening alarm perspective.");
            LOG.catching(ex);
        }

    }
}
