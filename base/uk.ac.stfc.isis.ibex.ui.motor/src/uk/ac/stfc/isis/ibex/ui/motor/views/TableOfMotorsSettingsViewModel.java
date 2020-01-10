package uk.ac.stfc.isis.ibex.ui.motor.views;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.typed.BeanProperties;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.motor.internal.MotorTableSettings;

/**
 * The view model for the table of motor settings.
 */
public class TableOfMotorsSettingsViewModel extends ModelObject {
    private boolean advancedMinimalMotorView = false;  // Set to false by default for standard view
	private DataBindingContext bindingContext = new DataBindingContext();

	public TableOfMotorsSettingsViewModel(MotorTableSettings motorSettingsModel) {
		bindingContext.bindValue(BeanProperties.value("advancedMinimalMotorView").observe(this),
				BeanProperties.value("advancedMinimalMotorView").observe(motorSettingsModel));
	}

	public Boolean getAdvancedMinimalMotorView() {
		return advancedMinimalMotorView;
	}

	public void setAdvancedMinimalMotorView(Boolean newSetting) {
		firePropertyChange("advancedMinimalMotorView", this.advancedMinimalMotorView, this.advancedMinimalMotorView = newSetting);
	}
}
