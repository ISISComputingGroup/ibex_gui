package uk.ac.stfc.isis.ibex.motor.internal;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class MotorSettings extends ModelObject {

	private boolean advancedMinimalMotorView;

	public boolean isAdvancedMinimalMotorView() {
		return advancedMinimalMotorView;
	}

	public void setAdvancedMinimalMotorView(boolean newSetting) {
		firePropertyChange("advancedMinimalMotorView", this.advancedMinimalMotorView, this.advancedMinimalMotorView = newSetting);
	}
	
}
