package uk.ac.stfc.isis.ibex.ui.banner.models;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.banner.BumpStopState;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorColours;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorViewStateConverter;

public class BumpStopViewState implements IndicatorViewStateConverter<BumpStopState> {
	private BumpStopState state;
	
	public void setState(BumpStopState state) {
		if (state == null) {
			this.state = BumpStopState.UNKNOWN;
		} else {
			this.state = state;
		}
	}
	
	public String getName() {
		return "Bump strip is " + state.name();
	}
	
	public Color color() {
		switch (state) { 
			case NOT_TRIPPED:
			case UNAVAILABLE:
				return IndicatorColours.BLACK;

			case TRIPPED:
			case UNKNOWN:
			default:
				return IndicatorColours.RED;			
		}
	}
	
	public Boolean toBool() {
		switch (state) {
			case TRIPPED:
				return true;
			case NOT_TRIPPED:
			case UNAVAILABLE:
			case UNKNOWN:
			default:
				return false;
		}
	}
	
	public Boolean availability() {
		switch (state) {
			case UNAVAILABLE:
				return false;
			case NOT_TRIPPED:
			case TRIPPED:
			case UNKNOWN:
			default:
				return true;
			}
		

	}	
	
}
