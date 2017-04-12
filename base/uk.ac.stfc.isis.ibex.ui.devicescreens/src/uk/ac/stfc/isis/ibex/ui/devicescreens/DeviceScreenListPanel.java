
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceDescription;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.opis.OPIViewCreationException;
import uk.ac.stfc.isis.ibex.ui.devicescreens.commands.ConfigureDeviceScreensHandler;
import uk.ac.stfc.isis.ibex.ui.devicescreens.list.DeviceScreensTable;
import uk.ac.stfc.isis.ibex.ui.devicescreens.models.ViewDeviceScreensDescriptionViewModel;

/**
 * A UI Panel for the devices screens.
 */
public class DeviceScreenListPanel extends Composite {

    /**
     * Logger.
     */
    private static final Logger LOG = IsisLog.getLogger(DeviceScreenListPanel.class);

    private Button configureDevScreensButton;

    private DeviceScreensTable deviceScreenList;

    private ConfigureDeviceScreensHandler configureDeviceScreensHandler = new ConfigureDeviceScreensHandler();

    /**
     * Create a Devices Screen Panel.
     * 
     * @param parent
     *            parent component
     * @param style
     *            SWT Style
     * @param viewModel
     *            the view model to be used by this view
     */
    public DeviceScreenListPanel(final Composite parent, int style, ViewDeviceScreensDescriptionViewModel viewModel,
            final DevicesOpiTargetView targetView) {
        super(parent, style);
        setLayout(new FillLayout(SWT.HORIZONTAL));

        // TODO temporarily place list and button in control, remove when
        // editing functionality is moved to configuration dialogue
        Composite composite = new Composite(this, SWT.NONE);

        GridLayout compositeLayout = new GridLayout(1, true);
        composite.setLayout(compositeLayout);

        deviceScreenList = new DeviceScreensTable(composite, SWT.BORDER, SWT.FULL_SELECTION);
        GridData devicesListLayout = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        deviceScreenList.setLayoutData(devicesListLayout);

        deviceScreenList.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {

                for (DeviceDescription deviceScreeen : deviceScreenList.selectedRows()) {
                    LOG.info("Open opi target " + deviceScreeen.getName());
                    try {
                        targetView.displayOpi(deviceScreeen.getOPITarget());
                    } catch (OPIViewCreationException e) {
                        LOG.catching(e);
                        MessageDialog.openError(parent.getShell(), "Error displaying OPI", e.getMessage());
                    }
                }

            }
        });


        configureDevScreensButton = new Button(composite, SWT.NONE);
        configureDevScreensButton.setText("Edit Device Screens");
        GridData gdconfigureDevScreensButton = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
        configureDevScreensButton.setLayoutData(gdconfigureDevScreensButton);

        configureDevScreensButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                try {
                    configureDeviceScreensHandler.execute(new ExecutionEvent());
                } catch (ExecutionException ex) {
                    LOG.catching(ex);
                    MessageDialog.openError(parent.getShell(), "Error displaying config dialogue", ex.getMessage());
                }
            }
        });

        viewModel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent deviceScreenDescription) {
                updateDeviceScreensDescriptions((DeviceScreensDescription) deviceScreenDescription.getNewValue());

            }
        });

        // Force the device screens to update when they are first created.
        updateDeviceScreensDescriptions(viewModel.getDeviceScreensDescription());

    }

    /**
     * Updates the device screen description in the GUI thread.
     * 
     * @param deviceScreensDescription
     *            the new device screens description
     */
    protected void updateDeviceScreensDescriptions(final DeviceScreensDescription deviceScreensDescription) {

        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (deviceScreenList != null && deviceScreensDescription != null) {
                    deviceScreenList.setRows(deviceScreensDescription.getDevices());
                }
            }
        });
    }

}
