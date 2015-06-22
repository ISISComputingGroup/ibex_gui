package uk.ac.stfc.isis.ibex.ui.banner.indicators;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public interface IndicatorModel {
	public UpdatedValue<String> text();
	public UpdatedValue<Color> color();
	public UpdatedValue<Boolean> availability();
}
