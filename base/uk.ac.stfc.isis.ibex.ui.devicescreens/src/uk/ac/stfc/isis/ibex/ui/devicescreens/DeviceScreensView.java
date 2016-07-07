
/*
 * This file is part of the ISIS IBEX application.
 * Copyright (C) 2012-2016 Science & Technology Facilities Council.
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

/**
 * 
 */
package uk.ac.stfc.isis.ibex.ui.devicescreens;

import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.ui.targets.DevicesTargetView;

/**
 * 
 */
public class DeviceScreensView extends ViewPart {

    public static final String ID = "uk.ac.stfc.isis.ibex.ui.devicescreens.devicescreensview";

    private static final Logger LOG = IsisLog.getLogger(DeviceScreensView.class);

    public DeviceScreensView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new FillLayout(SWT.HORIZONTAL));
//        ExperimentDetailsPanel experimentDetails = new ExperimentDetailsPanel(parent, SWT.NONE);
        Panel panel = new Panel(parent, SWT.NONE);
        displayOpi("Lakeshore 218");
    }

    @Override
    public void setFocus() {
    }

    private void displayOpi(String name) {
        try {
            IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            DevicesTargetView view = (DevicesTargetView) workbenchPage.showView(DevicesTargetView.ID);
            view.setOpi(name);
        } catch (PartInitException e) {
            LOG.catching(e);
        }
    }
}