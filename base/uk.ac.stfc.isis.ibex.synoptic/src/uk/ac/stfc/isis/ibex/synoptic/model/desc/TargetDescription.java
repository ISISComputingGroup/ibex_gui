package uk.ac.stfc.isis.ibex.synoptic.model.desc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/*
 * Describes the target for navigation around the synoptic
 */
@XmlRootElement(name = "pv")
@XmlAccessorType(XmlAccessType.FIELD)
public class TargetDescription {

	private String name;
	private TargetType type;
	
	@XmlElementWrapper(name = "properties")
	@XmlElement(name = "property", type = Property.class)
	private ArrayList<Property> properties = new ArrayList<>();
	
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
	
	public List<Property> properties() {
		return Collections.unmodifiableList(properties);
	}
	
	public boolean addProperty(Property property) {
		return properties.add(property);
	}
	
	public boolean removeProperty(Object property) {
		return properties.remove(property);
	}

	public void replaceProperty(Property current, Property newProperty) {
		int index = properties.indexOf(current);
		if (index != -1) {
			properties.set(index, newProperty);
		}
	}
}
