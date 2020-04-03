package uk.ac.stfc.isis.ibex.ui.motor.views;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.motor.internal.MotorsTableSettings;

/**
 * The view model for the table of motor settings.
 */
public class TableOfMotorsSettingsViewModel extends ModelObject {
    private boolean advancedMinimalMotorView = false;  // Set to false by default for standard view
	private DataBindingContext bindingContext = new DataBindingContext();

	/**
	 * The view model for the setting tab in the table of motors.
	 * @param motorSettingsModel the underlying model
	 */
	public TableOfMotorsSettingsViewModel(MotorsTableSettings motorSettingsModel) {
		bindingContext.bindValue(BeanProperties.value("advancedMinimalMotorView").observe(this),
				BeanProperties.value("advancedMinimalMotorView").observe(motorSettingsModel));
	}

	/**
	 * Gets whether the table of motors is in "advanced" mode.
	 * @return true if the table is in advanced mode, false otherwise
	 */
	public boolean getAdvancedMinimalMotorView() {
		return advancedMinimalMotorView;
	}

	/**
	 * Sets whether the table of motors is in "advanced" mode.
	 * param newSetting true if the table is in advanced mode, false otherwise
	 */
	public void setAdvancedMinimalMotorView(boolean newSetting) {
		firePropertyChange("advancedMinimalMotorView", this.advancedMinimalMotorView, this.advancedMinimalMotorView = newSetting);
	}
}
