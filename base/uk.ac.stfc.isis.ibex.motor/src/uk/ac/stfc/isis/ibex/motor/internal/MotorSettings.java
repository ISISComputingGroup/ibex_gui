package uk.ac.stfc.isis.ibex.motor.internal;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class MotorSettings extends ModelObject {

	private boolean enableAdvanceMotorView;

	public boolean getEnableAdvanceMotorView() {
		return enableAdvanceMotorView;
	}

	public void setEnableAdvanceMotorView(boolean enableAdvanceMotorView) {
		firePropertyChange("enableAdvanceMotorView", enableAdvanceMotorView, this.enableAdvanceMotorView = enableAdvanceMotorView);
		System.out.println("Value = " + enableAdvanceMotorView);
	}
	
}
