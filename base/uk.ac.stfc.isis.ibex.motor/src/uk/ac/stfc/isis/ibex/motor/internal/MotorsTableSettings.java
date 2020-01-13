package uk.ac.stfc.isis.ibex.motor.internal;

import uk.ac.stfc.isis.ibex.model.ModelObject;

/**
 * The model for the table of motor settings.
 */
public class MotorsTableSettings extends ModelObject {

	/**
	 * The table of motors can display the standard or advanced minimal motor view depending on the
	 * users requirements
	 */
	private boolean advancedMinimalMotorView;

	public boolean isAdvancedMinimalMotorView() {
		return advancedMinimalMotorView;
	}

	public void setAdvancedMinimalMotorView(boolean newSetting) {
		firePropertyChange("advancedMinimalMotorView", this.advancedMinimalMotorView, this.advancedMinimalMotorView = newSetting);
	}
	
}
