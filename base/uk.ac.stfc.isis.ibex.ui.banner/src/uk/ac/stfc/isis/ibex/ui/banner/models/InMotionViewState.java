package uk.ac.stfc.isis.ibex.ui.banner.models;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.banner.InMotionState;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorColours;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorViewStateConverter;

public class InMotionViewState implements IndicatorViewStateConverter<InMotionState> {
	private InMotionState state;
	
	public void setState(InMotionState state) {
		if (state == null)
		{
			this.state = InMotionState.UNKNOWN;
		} else {
			this.state = state;
		}
	}
	
	public String getName() {
		return "Motors are " + state.name();
	}
	
	public Color color() {
		switch (state) { 
			case STATIONARY:		
				return IndicatorColours.BLACK;
			case MOVING:
			case UNKNOWN:
			default:
				return IndicatorColours.RED;			
		}
	}
	
	public Boolean toBool() {
		switch (state) {
			case MOVING:
				return true;
			case STATIONARY:
			case UNKNOWN:
			default:
				return false;
		}
	}
}
