
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

package uk.ac.stfc.isis.ibex.ui.devicescreens;

import org.apache.logging.log4j.Logger;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.devicescreens.DeviceScreens;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescriptionXmlParser;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.observing.BaseObserver;
import uk.ac.stfc.isis.ibex.epics.observing.ForwardingObservable;
import uk.ac.stfc.isis.ibex.epics.observing.Observer;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.ui.devicescreens.list.DeviceScreensTable;

/**
 * A UI Panel for the devices screens.
 */
public class Panel extends Composite {

    /**
     * Logger.
     */
    private static final Logger LOG = IsisLog.getLogger(Panel.class);

    private Text txtScreensSp;
    private Writable<DeviceScreensDescription> screensSetter;

    private final Display display = Display.getCurrent();
    private final Observer<DeviceScreensDescription> pvObserver = new BaseObserver<DeviceScreensDescription>() {
        @Override
        public void onValue(final DeviceScreensDescription value) {
            display.asyncExec(new Runnable() {
                @Override
                public void run() {
                    // setLabelValue(value.toString());
                    deviceScreenList.setRows(value.getDevices());
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
    private DeviceScreensTable deviceScreenList;

    /**
     * Create a Devices Screen Panel
     * 
     * @param parent parent component
     * @param style SWT Style
     */
    public Panel(Composite parent, int style) {
        super(parent, style);
        setLayout(new FillLayout(SWT.HORIZONTAL));

        // TODO temporarily place list and setter in control, remove when
        // editing functionality is added
        Composite composite = new Composite(this, SWT.NONE);

        GridLayout compositeLayout = new GridLayout(2, true);
        composite.setLayout(compositeLayout);

        deviceScreenList = new DeviceScreensTable(composite, SWT.BORDER, SWT.V_SCROLL | SWT.NO_SCROLL | SWT.FULL_SELECTION);
        GridData devicesListLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        deviceScreenList.setLayoutData(devicesListLayout);

        DeviceScreens deviceScreens = DeviceScreens.getInstance();
        ForwardingObservable<DeviceScreensDescription> availableScreensObservable = deviceScreens.getDevices();
        availableScreensObservable.addObserver(pvObserver);

        deviceScreenList.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                for (DeviceDescription deviceScreeen : deviceScreenList.selectedRows()) {
                    displayOpi(deviceScreeen.getName(), deviceScreeen.getKey());
                }

            }
        });

        txtScreensSp = new Text(composite, SWT.WRAP | SWT.MULTI);
        GridData gdTxtScreens = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
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
            // Ignore - this is just a temporary testing panel anyway
        }
    }

    /**
     * Display an OPI view on the page
     * 
     * @param title - Title for the OPI
     * @param opiName - Name of the OPI used to identify it from available list
     */
    private void displayOpi(String title, String opiName) {
        try {
            IWorkbenchPage workbenchPage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            DevicesOpiTargetView view = (DevicesOpiTargetView) workbenchPage.showView(DevicesOpiTargetView.ID);
            view.setOpi(title, opiName);
        } catch (PartInitException e) {
            LOG.catching(e);
        }
    }


}
