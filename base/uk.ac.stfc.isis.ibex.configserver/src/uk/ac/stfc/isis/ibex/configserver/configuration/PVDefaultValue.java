package uk.ac.stfc.isis.ibex.configserver.configuration;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class PVDefaultValue extends ModelObject {
	private String name;
	private String value;
	
	public PVDefaultValue(PVDefaultValue other) {
		this.name = other.name;
		this.value = other.value;
	}
	
	public PVDefaultValue(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		firePropertyChange("value", this.value, this.value = value);
	}
}
