
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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.devicescreens.DeviceScreens;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescriptionXmlParser;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;

/**
 * 
 */
public class Panel extends Composite {

    private Label lblScreensRbv;
    private Text txtScreensSp;
    private Writable<DeviceScreensDescription> screensSetter;

    private final Display display = Display.getCurrent();
    private final Observer<DeviceScreensDescription> pvObserver = new BaseObserver<DeviceScreensDescription>() {
        @Override
        public void onValue(final DeviceScreensDescription value) {
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
        lblScreensRbv.setText(value);

        // Need to call this, otherwise the label text will be set, but the view
        // won't be updated
        lblScreensRbv.getParent().layout();
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
        composite.setLayout(new GridLayout(2, false));

        DeviceScreens deviceScreens = DeviceScreens.getInstance();

        lblScreensRbv = new Label(composite, SWT.NONE);
        lblScreensRbv.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1));
        ForwardingObservable<DeviceScreensDescription> availableScreensObservable = deviceScreens.getDevices();
        availableScreensObservable.addObserver(pvObserver);

        txtScreensSp = new Text(composite, SWT.WRAP | SWT.MULTI);
        GridData gdTxtScreens = new GridData(SWT.LEFT, SWT.TOP, true, true, 1, 1);
        gdTxtScreens.heightHint = 2000;
        gdTxtScreens.widthHint = 2000;
        txtScreensSp.setLayoutData(gdTxtScreens);
        screensSetter = deviceScreens.getDevicesSetter();

        txtScreensSp.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent arg0) {
                setScreens();
            }
        });

    }

    private void setScreens() {
        String inputText = txtScreensSp.getText();
        DeviceScreensDescriptionXmlParser parser = new DeviceScreensDescriptionXmlParser();
        try {
            DeviceScreensDescription screens = parser.convert(inputText);
            screensSetter.write(screens);
        } catch (ConversionException e) {
            e.printStackTrace();
            // setScreens() gets called at each key stroke, so until a valid xml
            // is entered, there will be errors
            // Ignore - this is just for testing anyway
        }
    }


}
