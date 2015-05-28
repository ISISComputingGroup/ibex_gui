package uk.ac.stfc.isis.ibex.experimentdetails;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class UserDetails extends ModelObject {
	
	private String name;
	private String institute;
	private Role role;

	public UserDetails(String name, String institute, Role role) {
		this.name = name;
		this.institute = institute;
		this.role = role;
	}
	
	public UserDetails(UserDetails other) {
		this(other.name, other.institute, other.role);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	public String getInstitute() {
		return institute;
	}

	public void setInstitute(String institute) {
		firePropertyChange("institute", this.institute, this.institute = institute);
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		firePropertyChange("role", this.role, this.role = role);
	}

}
