
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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class describes the properties element of the device screens xml format.
 * 
 * Note that it may be possible to eliminate this class by using an
 * @XmlElementWrapper but it's not working for me right now.
 * 
 * Note any changes here will require corresponding changes to
 * EPICS/schema/configurations/screens.xsd. 
 */
@XmlRootElement(name = "properties")
public class PropertiesDescription {

    @XmlElement(name = "property", type = PropertyDescription.class)
    private List<PropertyDescription> properties = new ArrayList<>();

    public List<PropertyDescription> getProperties() {
        return properties;
    }
}
