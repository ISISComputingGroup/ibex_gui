package uk.ac.stfc.isis.ibex.configserver.configuration;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class Macro extends ModelObject {
	private String name;
	private String value;
	private String description;
	private String pattern;
	
	public Macro(Macro other) {
		this(other.getName(), other.getValue(), other.getDescription(), other.getPattern());
	}
	
	public Macro(String name, String value, String description, String pattern) {
		this.name = name;
		this.value = value;
		this.description = description;
		this.pattern = pattern;
	}
	
	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}
	
	public String getName() {
		return name;
	}
	
	public void setValue(String value) {
		firePropertyChange("value", this.value, this.value = value);
	}
	
	public String getValue() {
		return value;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getPattern() {
		return pattern;
	}
	
	@Override
	public String toString() {
		return name + "=" + value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Macro)) {
			return false;
		}
		
		if (obj == this) {
			return true;
		}
		
		Macro other = (Macro) obj;
		return name.equals(other.name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
