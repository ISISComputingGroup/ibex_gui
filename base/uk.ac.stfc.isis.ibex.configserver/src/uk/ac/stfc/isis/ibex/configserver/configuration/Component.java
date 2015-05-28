// A component that may be included in a config
package uk.ac.stfc.isis.ibex.configserver.configuration;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class Component extends ModelObject implements Comparable<Component> {
	
	private final String name;
	private String description = "";
	private String pv = "";
	
	public Component(String name) {
		this.name = name;
	}
	
	public Component(Component other) {
		this.name = other.name;
		this.description = other.description;
		this.pv = other.pv();
	}
	
	public String getName() {
		return name;
	}

	public String description() {
		return description;
	}
	
	public String pv() {
		return pv;
	}
	
	@Override
	public int compareTo(Component other) {
		return name.compareTo(other.name);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Component)) {
			return false;
		}
		
		if (obj == this) {
			return true;
		}
		
		Component other = (Component) obj;
		return name.equals(other.name);
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
