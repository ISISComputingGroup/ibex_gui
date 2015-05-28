package uk.ac.stfc.isis.ibex.ui.banner.models;

import org.eclipse.swt.graphics.Color;

import uk.ac.stfc.isis.ibex.epics.pv.Closer;
import uk.ac.stfc.isis.ibex.instrument.baton.BannerObservables;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.ui.banner.indicators.IndicatorModel;

public class BatonUserModel extends Closer implements IndicatorModel {
	
	private BatonUserObserver batonUser;
	
	public BatonUserModel(BannerObservables observables) {
		batonUser = registerForClose(new BatonUserObserver(observables.controlPV, observables.self));
	}
	
	public UpdatedValue<String> text() {
		return batonUser.text();
	}

	public UpdatedValue<Color> color() {
		return batonUser.color();
	}
	
}
