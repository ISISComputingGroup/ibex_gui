package uk.ac.stfc.isis.ibex.experimentdetails;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class Parameter extends ModelObject {

	private String name;
	private String units;
	private String value;

	public String getName() {
		return name;
	}
	
	protected void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}
	
	public String getUnits() {
		return units;
	}
	
	protected void setUnits(String units) {
		firePropertyChange("units", this.units, this.units = units);
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		firePropertyChange("value", this.value, this.value = value);
	}
}
