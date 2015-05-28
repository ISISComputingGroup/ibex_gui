package uk.ac.stfc.isis.ibex.ui.banner.controls;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public interface ControlModel {
	public void click();
	public String text();
	public UpdatedValue<Boolean> enabled();
}
