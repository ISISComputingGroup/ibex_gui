package uk.ac.stfc.isis.ibex.ui.motor.views;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.motor.Motors;

/**
 * The view model for the table of motor settings.
 */
public final class TableOfMotorsSettingsViewModel extends ModelObject {
    private boolean advancedMinimalMotorView = false;
	private DataBindingContext bindingContext = new DataBindingContext();
	
	private static final TableOfMotorsSettingsViewModel INSTANCE = new TableOfMotorsSettingsViewModel();

	/**
	 * The view model for the setting tab in the table of motors.
	 */
	private TableOfMotorsSettingsViewModel() {
		bindingContext.bindValue(BeanProperties.value("advancedMinimalMotorView").observe(this),
				BeanProperties.value("advancedMinimalMotorView").observe(Motors.getInstance().getMotorSettingsModel()));
	}
	
	/**
	 * Gets the singleton instance of this class.
	 * @return the singleton instance of this class
	 */
	public static TableOfMotorsSettingsViewModel getInstance() {
		return INSTANCE;
	}

	/**
	 * Gets whether the table of motors is in "advanced" mode.
	 * @return true if the table is in advanced mode, false otherwise
	 */
	public boolean isAdvancedMinimalMotorView() {
		return advancedMinimalMotorView;
	}

	/**
	 * Sets whether the table of motors is in "advanced" mode.
	 * @param newSetting true if the table is in advanced mode, false otherwise
	 */
	public void setAdvancedMinimalMotorView(boolean newSetting) {
		firePropertyChange("advancedMinimalMotorView", this.advancedMinimalMotorView, this.advancedMinimalMotorView = newSetting);
	}
}
