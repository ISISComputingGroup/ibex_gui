
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import uk.ac.stfc.isis.ibex.devicescreens.DeviceScreens;
import uk.ac.stfc.isis.ibex.ui.devicescreens.models.ViewDeviceScreensDescriptionViewModel;

/**
 * A view for the current available device screens.
 */
public class DeviceScreensView extends ViewPart {

    /**
     * Class ID.
     */
    public static final String ID = "uk.ac.stfc.isis.ibex.ui.devicescreens.devicescreensview";

    /**
     * Default constructor.
     */
    public DeviceScreensView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new FillLayout(SWT.HORIZONTAL));
        new DeviceScreenListPanel(parent, SWT.NONE,
                new ViewDeviceScreensDescriptionViewModel(DeviceScreens.getInstance().getModel()));
    }

    @Override
    public void setFocus() {
    }
}