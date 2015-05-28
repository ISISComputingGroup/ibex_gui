package uk.ac.stfc.isis.ibex.configserver.configuration;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class PVSet extends ModelObject {
	private String name;
	private boolean enabled;
	
	public PVSet(PVSet other) {
		this.name = other.name;
		this.enabled = other.enabled;
	}
	
	public PVSet(String name, boolean enabled) {
		this.name = name;
		this.enabled = enabled;
	}

	public String getName() {
		return name;
	}
	
	public boolean getEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		firePropertyChange("enabled", this.enabled, this.enabled = enabled);
	}
}
