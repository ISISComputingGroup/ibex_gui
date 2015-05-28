package uk.ac.stfc.isis.ibex.configserver.configuration;

public class AvailablePV {
	private String name;
	private String description;
	
	public AvailablePV(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public AvailablePV(AvailablePV other) {
		this.name = other.name;
		this.description = other.description;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
