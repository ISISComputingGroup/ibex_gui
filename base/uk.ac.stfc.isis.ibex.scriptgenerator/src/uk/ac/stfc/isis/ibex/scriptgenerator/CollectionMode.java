package uk.ac.stfc.isis.ibex.scriptgenerator;

public enum CollectionMode {
	HISTOGRAM ("Histogram"),
	EVENTS ("Events");
	
	private String name;
	
	private CollectionMode(String displayName) {
		this.name = displayName;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
