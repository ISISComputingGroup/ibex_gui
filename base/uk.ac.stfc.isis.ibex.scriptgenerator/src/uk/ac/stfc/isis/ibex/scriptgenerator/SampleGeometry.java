package uk.ac.stfc.isis.ibex.scriptgenerator;

public enum SampleGeometry {
	DISC ("Disc"),
	CYLINDRICAL ("Cylindrical"),
	FLATPLATE ("Flat Plate"),
	SINGLECRYSTAL ("Single Crystal");
	
	private String name;
	
	private SampleGeometry(String displayName) {
		this.name = displayName;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
