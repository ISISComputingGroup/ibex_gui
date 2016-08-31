package uk.ac.stfc.isis.ibex.scriptgenerator;

public enum ApertureTrans {
	LARGE ("Large = ?, ? mm"),
	MEDIUM ("Medium = 20, 14 mm"),
	SMALL ("Small = ?, ? mm");
	
	private String name;
	
	private ApertureTrans(String displayName) {
		this.name = displayName;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
