
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
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
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.devicescreens.xml.XMLUtil;

/**
 * This class describes the devices element of the device screens xml format.
 * 
 * Note any changes here will require corresponding changes to
 * EPICS/schema/configurations/screens.xsd.
 */
@XmlRootElement(name = "devices")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceScreensDescription {

    @XmlElement(name = "device", type = DeviceDescription.class)
    private ArrayList<DeviceDescription> devices = new ArrayList<>();

    /**
     * Getter for the list of devices, for testing purposes.
     * 
     * @return the list of devices
     */
    public List<DeviceDescription> getDevices() {
        return devices;
    }

    /**
     * Adds a device to the list of device descriptions.
     * 
     * @param device the device description to add
     */
    public void addDevice(DeviceDescription device) {
        devices.add(device);
    }

    @Override
    public String toString() {
        try {
            return XMLUtil.toXml(this).replaceAll("><", ">\n<");
        } catch (JAXBException e) {
            e.printStackTrace();
            return e.toString();
        } catch (SAXException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}
