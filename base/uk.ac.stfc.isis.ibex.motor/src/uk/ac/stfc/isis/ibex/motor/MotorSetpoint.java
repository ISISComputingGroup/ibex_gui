package uk.ac.stfc.isis.ibex.motor;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public abstract class MotorSetpoint extends ModelObject {

	private final String name;
	
	public MotorSetpoint(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract Double getValue();

	public abstract Double getSetpoint();
	
	public abstract void setSetpoint(double value);
	
	public abstract void home();

	public abstract Boolean canHome();	
}
