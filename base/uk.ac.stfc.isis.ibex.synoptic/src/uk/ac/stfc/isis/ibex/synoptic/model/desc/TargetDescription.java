
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * Describes the target for navigation around the synoptic.
 */
@XmlRootElement(name = "target")
@XmlAccessorType(XmlAccessType.FIELD)
public class TargetDescription extends ModelObject {

    private String name;
    private TargetType type;
	
	@XmlElementWrapper(name = "properties")
	@XmlElement(name = "property", type = Property.class)
	private ArrayList<Property> properties = new ArrayList<>();
	
    /**
     * Default constructor, required due to existence of copy constructor.
     */
    public TargetDescription() {
    }

    /**
     * Instantiates a new target description.
     *
     * @param name the name
     * @param type the type, e.g. OPI
     */
    public TargetDescription(String name, TargetType type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Copy constructor.
     * 
     * @param other
     *            TargetDescription to be copied
     */
    public TargetDescription(TargetDescription other) {
        this.name = other.name;
        this.type = other.type;
        this.properties = new ArrayList<>(other.getProperties());
    }

    /**
     * Name.
     *
     * @return the string
     */
	public String name() {
		return name;
	}

    /**
     * Type, e.g. OPI.
     *
     * @return the target type
     */
	public TargetType type() {
		return type;
	}	
	
    /**
     * Sets the name.
     *
     * @param name the new name
     */
	public void setName(String name) {
		this.name = name;
	}
	
    /**
     * Sets the type.
     *
     * @param type the new type
     */
	public void setType(TargetType type) {
		this.type = type;
	}

    /**
     * Adds possibly the property names; default blank properties are added.
     *
     * @param propertyKeys the property keys
     */
    public void addProperties(List<String> propertyKeys) {
        for (String key : propertyKeys) {
            if (!this.containsProperty(key)) {
                properties.add(new Property(key, ""));
            }
        }
    }

    /**
     * Does this target contain a property key.
     *
     * @param key the key
     * @return true, if successful
     */
    public boolean containsProperty(String key) {
        for (Property property : this.properties) {
            if (key.equals(property.getKey())) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
	public List<Property> getProperties() {
		return Collections.unmodifiableList(properties);
	}

    /**
     * Gets the property; or return the default.
     *
     * @param key the key of the property to find
     * @param defaultProperty the default property to return if the key doesn't
     *            exist; can be null
     * @return the property; or default if it doesn't exist
     */
    public Property getProperty(String key, Property defaultProperty) {
        for (Property property : this.properties) {
            if (key.equals(property.getKey())) {
                return property;
            }
        }

        return defaultProperty;
    }
}
