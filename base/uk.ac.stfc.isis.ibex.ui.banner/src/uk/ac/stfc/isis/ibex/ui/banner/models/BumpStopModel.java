package uk.ac.stfc.isis.ibex.ui.banner.models;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.banner.BumpStopState;
import uk.ac.stfc.isis.ibex.banner.Observables;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorModel;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorStateObserver;

public class BumpStopModel extends Closer implements IndicatorModel {
	
	private IndicatorStateObserver<BumpStopState> bumpState;
	
	public BumpStopModel(Observables observables) {
		bumpState = registerForClose(new IndicatorStateObserver<BumpStopState>(observables.bumpStop, new BumpStopViewState()));
	}
	
	public UpdatedValue<String> text() {
		return bumpState.text();
	}

	public UpdatedValue<Color> color() {
		return bumpState.color();
	}
	
}
