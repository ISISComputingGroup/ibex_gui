package uk.ac.stfc.isis.ibex.users;

public abstract class User {
	
	public abstract String name();
	
	public abstract boolean canEditBlocks();
	
	public boolean requiresPassword() {
		return password() != "";
	}
	
	public String password() {
		return "";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null || !(obj instanceof User)) {
			return false;
		}
		
		User other = (User) obj;
		return this.name().equals(other.name());
	}
	
	@Override
	public int hashCode() {
		return this.name().hashCode();
	}
}
