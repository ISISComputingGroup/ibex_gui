package uk.ac.stfc.isis.ibex.ui.motor.views;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.motor.internal.MotorSettings;

/**
 * The view model for the table of motor settings.
 */
public class TableOfMotorsSettingsViewModel extends ModelObject {
    private boolean enableAdvanceMotorView = false;
	private MotorSettings motorSettingsModel;
	private DataBindingContext bindingContext = new DataBindingContext();

	public TableOfMotorsSettingsViewModel(MotorSettings motorSettingsModel) {
		this.motorSettingsModel = motorSettingsModel;
		bindingContext.bindValue(BeanProperties.value("enableAdvanceMotorView").observe(this),
				BeanProperties.value("enableAdvanceMotorView").observe(motorSettingsModel));
	}

	public Boolean getEnableAdvanceMotorView() {
		return enableAdvanceMotorView;
	}

	public void setEnableAdvanceMotorView(Boolean enableAdvanceMotorView) {
		firePropertyChange("enableAdvanceMotorView", this.enableAdvanceMotorView, this.enableAdvanceMotorView = enableAdvanceMotorView);
		// System.out.println("Value = " + enableAdvanceMotorView);
	}
}
