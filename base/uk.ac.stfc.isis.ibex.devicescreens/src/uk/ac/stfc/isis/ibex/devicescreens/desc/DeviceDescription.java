
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


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.targets.OpiTarget;

/**
 * This class describes the device element of the device screens XML format.
 * 
 * Note any changes here will require corresponding changes to
 * EPICS/schema/configurations/screens.xsd.
 */
@XmlRootElement(name = "device")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeviceDescription extends ModelObject {

    /** The device name. */
    private String name;

    /** The key is either the OPI name or custom screen name. */
    private String key;

    /** The type is either an OPI or a custom screen. */
    private String type;

    /** */
    private String persistence = "";

    /** The properties that have been set. */
    @XmlElement(name = "properties", type = PropertiesDescription.class)
    private PropertiesDescription properties = new PropertiesDescription();

    /**
     * Default constructor. Needed for parsing XML.
     */
    public DeviceDescription() {
    }

    /**
     * A copy constructor.
     * 
     * @param original the item to copy
     */
    public DeviceDescription(DeviceDescription original) {
        key = original.getKey();
        name = original.getName();
        type = original.getType();

        for (PropertyDescription p : original.getProperties()) {
            this.addProperty(new PropertyDescription(p.getKey(), p.getValue()));
        }
    }

    /**
     * @return the type, e.g. OPI
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the resource key, for an OPI this is the key within the OPI
     *         descriptions file, e.g. Eurotherm
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        firePropertyChange("key", this.key, this.key = key);
    }

    public String getPersistence() {
        System.out.println("DEBUG 2");
        return persistence;
    }

    public void setPersistence(String persistence) {
        System.out.println("DEBUG 3");
        firePropertyChange("persistence", this.persistence, this.persistence = persistence);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        firePropertyChange("name", this.name, this.name = name);
    }

    /**
     * @return the list of properties
     */
    public List<PropertyDescription> getProperties() {
        return properties.getProperties();
    }

    /**
     * Clears out the properties.
     */
    public void clearProperties() {
        properties = new PropertiesDescription();
    }

    /**
     * @param property the property to add
     */
    public void addProperty(PropertyDescription property) {
        properties.addProperty(property);
    }

    /**
     * Convert the Device description to an OPI target.
     * 
     * @return OPI Target
     */
    public OpiTarget getOPITarget() {
        OpiTarget target = new OpiTarget(getName(), getKey());
        for (PropertyDescription property : getProperties()) {
            target.addProperty(property.getKey(), property.getValue());
        }
        return target;
    }
}
