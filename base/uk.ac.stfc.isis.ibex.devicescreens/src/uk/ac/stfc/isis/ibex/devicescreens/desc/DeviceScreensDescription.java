
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2015
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

package uk.ac.stfc.isis.ibex.devicescreens.desc;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class describes the devices element of the device screens xml format.
 * 
 * Note any changes here will require corresponding changes to
 * EPICS/schema/configurations/screens.xsd.
 */
@XmlRootElement(name = "devices", namespace = "http://epics.isis.rl.ac.uk/schema/screens/1.0/")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceScreensDescription {

    @XmlElement(name = "device", type = DeviceDescription.class)
    private ArrayList<DeviceDescription> devices = new ArrayList<>();

    @Override
    public String toString() {
        String toReturn = "Devices [\n";
        for (DeviceDescription device : devices)
        {
            toReturn += device.toString();
        }
        toReturn += "]\n";
        return toReturn;
    }
}
