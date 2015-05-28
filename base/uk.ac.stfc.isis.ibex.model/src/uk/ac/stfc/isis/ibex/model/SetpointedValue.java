package uk.ac.stfc.isis.ibex.model;


public abstract class SetpointedValue extends ModelObject {

	private final String name;
	
	public SetpointedValue(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract Double getValue();

	public abstract Double getSetpoint();
	
	public abstract Boolean canHome();
	
	public abstract void home();
}
