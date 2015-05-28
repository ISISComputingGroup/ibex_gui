package uk.ac.stfc.isis.ibex.ui.banner.models;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.banner.InMotionState;
import uk.ac.stfc.isis.ibex.banner.Observables;
import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorModel;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorStateObserver;

public class InMotionModel extends Closer implements IndicatorModel {
	
	private final IndicatorStateObserver<InMotionState> inMotion;
	
	public InMotionModel(Observables observables) {
		inMotion = registerForClose(new IndicatorStateObserver<InMotionState>(observables.inMotion, new InMotionViewState()));
	}
	
	public UpdatedValue<String> text() {
		return inMotion.text();
	}

	public UpdatedValue<Color> color() {
		return inMotion.color();
	}
	
}
