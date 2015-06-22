package uk.ac.stfc.isis.ibex.ui.banner.indicators;

import org.eclipse.swt.graphics.Color;

public interface IndicatorViewStateConverter<T> {
	public void setState(T state);
	public String getName();
	public Color color();
	public Boolean toBool();
	public Boolean availability();
}
