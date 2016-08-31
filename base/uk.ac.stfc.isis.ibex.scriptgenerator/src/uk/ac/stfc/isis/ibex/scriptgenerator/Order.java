package uk.ac.stfc.isis.ibex.scriptgenerator;

public enum Order {
	TRANS ("All TRANS first"),
	ALTTRANS ("Alternate - TRANS first"),
	SANS ("All SANS first"),
	ALTSANS ("Alternate - SANS first");
	
	private String name;
	
	private Order(String displayName) {
		this.name = displayName;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
