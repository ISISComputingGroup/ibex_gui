package uk.ac.stfc.isis.ibex.ui.motor.views;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.motor.Motors;

/**
 * The view model for the table of motor settings.
 */
public class TableOfMotorsSettingsViewModel extends ModelObject {
    private Boolean enableAdvanceMotorView = false;
	private Motors motorsModel;

	public TableOfMotorsSettingsViewModel(Motors motorsModel) {
		this.motorsModel = motorsModel;
	}

	public Boolean getEnableAdvanceMotorView() {
		return enableAdvanceMotorView;
	}

	public void setEnableAdvanceMotorView(Boolean enableAdvanceMotorView) {
		firePropertyChange("enableAdvanceMotorView", enableAdvanceMotorView, this.enableAdvanceMotorView = enableAdvanceMotorView);
		System.out.println("Value = " + enableAdvanceMotorView);
	}    
}
