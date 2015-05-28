package uk.ac.stfc.isis.ibex.configserver.configuration;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class PV extends ModelObject {
	
	private String address;
	private String type;
	private String description;
	private String iocName;
	
	public PV(String address, String type, String description, String iocName) {
		this.address = address;
		this.type = type;
		this.description = description;
		this.iocName = iocName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		firePropertyChange("address", this.address, this.address = address);
	}
	
	public String type() {
		return type;
	}

	public String description() {
		return description;
	}

	public String getDescription() {
		return description;
	}

	public String iocName() {
		return iocName;
	}
	
	@Override
	public String toString() {
		return address;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof PV)) {
			return false;
		}
		
		if (obj == this) {
			return true;
		}
		
		PV other = (PV) obj;
		return address.equals(other.address);
	}
	
	@Override
	public int hashCode() {
		return address.hashCode();
	}
}
