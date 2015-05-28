package uk.ac.stfc.isis.ibex.ui.configserver.editing.blocks.filters;

public enum SourceFilters {
	ALL ("All IOCs"),
	ACTIVE ("Active IOCs"),
	ASSOCIATED ("Config IOCs");
	
	private String displayName;
	
	private SourceFilters(String displayName) {
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
