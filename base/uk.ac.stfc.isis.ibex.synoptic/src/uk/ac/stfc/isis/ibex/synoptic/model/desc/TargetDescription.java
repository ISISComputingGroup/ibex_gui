
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
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

package uk.ac.stfc.isis.ibex.synoptic.model.desc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/*
 * Describes the target for navigation around the synoptic.
 */
@XmlRootElement(name = "target")
@XmlAccessorType(XmlAccessType.FIELD)
public class TargetDescription {

    private String name;
    private TargetType type;

    @XmlTransient
    private boolean userSelected = true;
	
	@XmlElementWrapper(name = "properties")
	@XmlElement(name = "property", type = Property.class)
	private ArrayList<Property> properties = new ArrayList<>();
	
    /**
     * Default constructor, required due to existence of copy constructor.
     */
    public TargetDescription() {
    }

    public TargetDescription(String name, TargetType type) {
        this.name = name;
        this.type = type;
        this.userSelected = false;
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
        this.userSelected = other.userSelected;
        this.properties = new ArrayList<>(other.properties);
    }

	public String name() {
		return name;
	}

	public TargetType type() {
		return type;
	}	
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setType(TargetType type) {
		this.type = type;
	}
	
    public boolean getUserSelected() {
        return this.userSelected;
    }

    public void setUserSelected(boolean userSelected) {
        this.userSelected = userSelected;
    }

    public void addProperties(List<String> propertyKeys) {
        for (String key : propertyKeys) {
            if (!this.containsProperty(key)) {
                properties.add(new Property(key, ""));
            }
        }
    }

    public boolean containsProperty(String key) {
        for (Property property : this.properties) {
            if (key.equals(property.key())) {
                return true;
            }
        }
        
        return false;
    }

    public void replaceProperty(Property current, Property newProperty) {
        int index = properties.indexOf(current);
        if (index != -1) {
            properties.set(index, newProperty);
        }
    }

    public void clearProperties() {
        this.properties = new ArrayList<>();
    }

	public List<Property> getProperties() {
		return Collections.unmodifiableList(properties);
	}

}
