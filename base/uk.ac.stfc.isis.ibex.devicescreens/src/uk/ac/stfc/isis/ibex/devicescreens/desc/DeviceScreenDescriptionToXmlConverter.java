
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
package uk.ac.stfc.isis.ibex.devicescreens.desc;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import uk.ac.stfc.isis.ibex.devicescreens.xml.XMLUtil;
import uk.ac.stfc.isis.ibex.epics.conversion.ConversionException;
import uk.ac.stfc.isis.ibex.epics.conversion.Converter;

/**
 * Converts a DeviceScreenDescription into a string in XML format.
 */
public class DeviceScreenDescriptionToXmlConverter extends Converter<DeviceScreensDescription, String> {

    /**
     * Returns the XML representation of a device screens.
     * 
     * @param value the input device screens to convert
     * @return the XML representation of the input
     * @throws ConversionException if a conversion error has occurred
     */
    @Override
    public String convert(DeviceScreensDescription value) throws ConversionException {
        try {
            return XMLUtil.toXml(value);
        } catch (JAXBException | SAXException e) {
            throw new ConversionException("Error converting device screens to XML", e);
        }
    }

}
