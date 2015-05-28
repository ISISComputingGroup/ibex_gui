package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters;

public enum InterestFilters {
	HIGH ("High"),
	MEDIUM ("Medium"),
	ALL ("All");
	
	private String displayName;
	
	private InterestFilters(String displayName) {
		this.displayName = displayName;
	}
	
	public String display() {
		return displayName;
	}
	
	@Override
	public String toString () {
		return displayName;
	}
}
