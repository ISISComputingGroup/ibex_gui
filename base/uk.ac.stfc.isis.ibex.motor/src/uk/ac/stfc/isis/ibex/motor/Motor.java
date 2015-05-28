package uk.ac.stfc.isis.ibex.motor;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public abstract class Motor extends ModelObject {

	public abstract String name();
	
	public abstract String motorAddress();

	public abstract String getDescription();
	
	public abstract MotorSetpoint getSetpoint();
	
	public abstract MotorEnable getEnabled();
	
	public abstract Double getLowerLimit();
		
	public abstract Double getUpperLimit();

	public abstract MotorDirection getDirection();
	
	public abstract Boolean getMoving();
	
	public abstract Boolean getAtHome();

	public abstract Boolean getAtLowerLimitSwtich();

	public abstract Boolean getAtUpperLimitSwitch();
	
	public abstract String getStatus();
}
