package uk.ac.stfc.isis.ibex.synoptic.model;

public abstract class ComponentProperty {
	
	private final String displayName;
	
	
	public ComponentProperty(String displayName) {
		this.displayName = displayName;
	}

	public String displayName() {
		return displayName;
	}
	
//	@Override
//	public boolean equals(Object obj) {
//		if (!(obj instanceof ComponentProperty)) {
//			return false;
//		}
//		
//		if (obj == this) {
//			return true;
//		}
//		
//		ComponentProperty other = (ComponentProperty) obj;
//		return displayName.equals(other.displayName);
//	}
//
//	@Override
//	public int hashCode() {
//		return displayName.hashCode();
//	}
}
