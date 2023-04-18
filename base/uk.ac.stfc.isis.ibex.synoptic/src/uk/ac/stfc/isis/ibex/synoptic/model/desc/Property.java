
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

package uk.ac.stfc.isis.ibex.synoptic.model.desc;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The Class Property which represents a property within a target. This are the
 * macro name and macro value for the target to have. This object is used in
 * serialisation from and to xml.
 */
@XmlRootElement(name = "targetproperty")
@XmlAccessorType(XmlAccessType.FIELD)
public class Property extends ModelObject {
	
	private String key;
	private String value;
	
    /**
     * Instantiates a new property. NB: Required for XML unmarshalling.
     */
	public Property() {
	}
	
	/**
	 * Clones an existing property.
	 * 
	 * @param other the property to clone
	 */
	public Property(Property other) {
		this(other.key, other.value);
	}
	
    /**
     * Instantiates a new property.
     *
     * @param key the key
     * @param value the value
     */
	public Property(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
    /**
     * Key.
     *
     * @return the string
     */
	public String getKey() {
		return key;
	}

    /**
     * Value.
     *
     * @return the string
     */
	public String getValue() {
		return value;
    }

	/**
	 * Sets the value of a property.
	 * 
	 * @param newValue The new value to set to.
	 */
	public void setValue(String newValue) {
		firePropertyChange("value", value, value = newValue);
	}
	
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Property other = (Property) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }

        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "Property [key=" + key + ", value=" + value + "]";
    }

}
