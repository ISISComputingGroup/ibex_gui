
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

package uk.ac.stfc.isis.ibex.devicescreens;

import org.apache.logging.log4j.Logger;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.epics.observing.Observable;
import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.logger.IsisLog;

/**
 * Describes a set of screens associated with the device screens perspective.
 */
public class DeviceScreens implements BundleActivator {

    /**
     * The logger to log device screens error messages.
     */
    public static final Logger LOG = IsisLog.getLogger(DeviceScreens.class);

    private static DeviceScreens instance;
    private static BundleContext context;

    private final DeviceScreenVariables variables;

    private DeviceScreensModel model;

    /**
     * Creates a new instance of the DevicesScreens class.
     */
    public DeviceScreens() {
    	
    	if (model != null) {
    		throw new RuntimeException("Can only create one DeviceScreensModel() instance at a time.");
    	}

        instance = this;

        variables = new DeviceScreenVariables();

        model = new DeviceScreensModel(getDevices(), getDevicesSetter());
    }

    /**
     * Gets the current instance of DeviceScreens.
     * 
     * @return the instance of DeviceScreens
     */
    public static DeviceScreens getInstance() {
        return instance;
    }

    /**
     * Gets the bundle context for this bundle activator.
     * 
     * @return the bundle context
     */
    public static BundleContext getContext() {
        return context;
    }

    /**
     * Gets an observable for the get device screens PV.
     * 
     * @return an observable to the get device screens PV
     */
    private Observable<DeviceScreensDescription> getDevices() {
        return variables.getDeviceScreens();
    }

    /**
     * Gets a writable for the set device screens PV.
     * 
     * @return a writable to the set device screens PV
     */
    public Writable<DeviceScreensDescription> getDevicesSetter() {
        return variables.getDeviceScreensSetter();
    }

    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.
     * BundleContext)
     */
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        DeviceScreens.context = bundleContext;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        DeviceScreens.context = null;
        model.close();
        model = null;
    }

    /**
     * Getter for the device screen variables with persistence information.
     * 
     * @return the device screen variables with persistence information.
     */
    public DeviceScreenVariables getVariables() {
        return variables;
    }

    /**
     * Gets the view model.
     * 
     * @return the view model
     */
    public DeviceScreensModel getModel() {
        return model;
    }

}

