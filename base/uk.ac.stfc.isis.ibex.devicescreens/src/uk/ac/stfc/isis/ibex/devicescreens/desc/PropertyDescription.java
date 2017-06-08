
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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * This class describes the property element of the device screens xml format.
 * 
 * Note any changes here will require corresponding changes to
 * EPICS/schema/configurations/screens.xsd. 
 */
@XmlRootElement(name = "property")
@XmlAccessorType(XmlAccessType.FIELD)
public class PropertyDescription extends ModelObject {

    private String key;
    private String value;
    
    /**
     * Default constructor. Needed for parsing XML.
     */
    public PropertyDescription() {
    }

    /**
     * Creates a new PropertyDescription given a key and a value.
     * 
     * @param key the property key
     * @param value the property value
     */
    public PropertyDescription(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Set the identifying key associated with the property.
     * 
     * @param newKey the new key
     */
    public void setKey(String newKey) {
        firePropertyChange("key", key, key = newKey);
    }

    /**
     * Set the value associated with the property.
     * 
     * @param newValue the new value
     */
    public void setValue(String newValue) {
        firePropertyChange("value", value, value = newValue);
    }

    /**
     * Get the identifying key associated with the property.
     * 
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Get the value associated with the property.
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
