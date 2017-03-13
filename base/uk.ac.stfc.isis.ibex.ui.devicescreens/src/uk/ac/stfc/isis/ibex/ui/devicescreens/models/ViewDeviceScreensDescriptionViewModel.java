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
package uk.ac.stfc.isis.ibex.ui.devicescreens.models;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;

import uk.ac.stfc.isis.ibex.devicescreens.DeviceScreensModel;
import uk.ac.stfc.isis.ibex.devicescreens.desc.DeviceScreensDescription;
import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The view model for the main device screens panel.
 */
public class ViewDeviceScreensDescriptionViewModel extends ModelObject {

    private DeviceScreensDescription deviceScreensDescription;
    private final DataBindingContext bindingContext = new DataBindingContext();

    /**
     * Constructor.
     * 
     * @param deviceScreensModel
     *            the device screens model.
     */
    public ViewDeviceScreensDescriptionViewModel(DeviceScreensModel deviceScreensModel) {
        bindingContext.bindValue(BeanProperties.value("deviceScreensDescription").observe(this),
                BeanProperties.value("deviceScreensDescription").observe(deviceScreensModel));
    }

    /**
     * @return the deviceScreensModel
     */
    public DeviceScreensDescription getDeviceScreensDescription() {
        return deviceScreensDescription;
    }

    /**
     * @param deviceScreensDescription
     *            the device screen description
     */
    public void setDeviceScreensDescription(DeviceScreensDescription deviceScreensDescription) {
        firePropertyChange("deviceScreensDescription", this.deviceScreensDescription,
                this.deviceScreensDescription = deviceScreensDescription);
    }


}
