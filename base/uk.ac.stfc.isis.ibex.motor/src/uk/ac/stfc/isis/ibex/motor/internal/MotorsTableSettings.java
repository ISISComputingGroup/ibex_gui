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

	/**
	 * Gets whether the advanced table of motors view is enabled or disabled.
	 * @return true if advanced mode is enabled; false otherwise
	 */
	public boolean isAdvancedMinimalMotorView() {
		return advancedMinimalMotorView;
	}

	/**
	 * Sets whether the advanced table of motors view is enabled or disabled.
	 * @param newSetting true if advanced mode is enabled; false otherwise
	 */
	public void setAdvancedMinimalMotorView(boolean newSetting) {
		firePropertyChange("advancedMinimalMotorView", this.advancedMinimalMotorView, this.advancedMinimalMotorView = newSetting);
	}
	
}
