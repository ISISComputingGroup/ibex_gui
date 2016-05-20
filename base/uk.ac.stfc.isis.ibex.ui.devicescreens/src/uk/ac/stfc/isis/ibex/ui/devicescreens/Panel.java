
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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import uk.ac.stfc.isis.ibex.devicescreens.DeviceScreens;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;

/**
 * 
 */
public class Panel extends Composite {

    private Label lblRbNumber;
    private final Display display = Display.getCurrent();
    private final Observer<DeviceScreensDescription> pvObserver = new BaseObserver<DeviceScreensDescription>() {
        @Override
        public void onValue(DeviceScreensDescription value) {
            display.asyncExec(new Runnable() {
                @Override
                public void run() {
                    setLabelValue(value.toString());
                }
            });
        }

        @Override
        public void onError(Exception e) {
            e.printStackTrace();
        }

        @Override
        public void onConnectionStatus(boolean isConnected) {
        }
    };

    private void setLabelValue(String value) {
        lblRbNumber.setText(value);

        // Need to call this, otherwise the label text will be set, but the view
        // won't be updated
        lblRbNumber.getParent().layout();
    }

    /**
     * @param parent
     * @param style
     */
    public Panel(Composite parent, int style) {
        // super(parent, style);
        super(parent, style);
        setLayout(new FillLayout(SWT.HORIZONTAL));

        Composite composite = new Composite(this, SWT.NONE);
        composite.setLayout(new GridLayout(5, false));

        lblRbNumber = new Label(composite, SWT.NONE);
        (new DeviceScreens()).getDevices().addObserver(pvObserver);
    }

}
